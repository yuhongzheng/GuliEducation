package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.CourseInfoForm;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.CourseQueryVo;
import com.atguigu.eduservice.entity.vo.CourseWebVo;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-12-07
 */
@Service
@Transactional
public class EduCourseServiceImpl extends ServiceImpl<EduCourseMapper, EduCourse> implements EduCourseService {
    @Autowired
    EduCourseDescriptionService descriptionService;
    @Autowired
    EduVideoService videoService;
    @Autowired
    EduChapterService chapterService;
    @Autowired
    VodClient vodClient;

    @Override
    public String addCourseInfo(CourseInfoForm courseInfoForm) {
        EduCourse course = new EduCourse();
        BeanUtils.copyProperties(courseInfoForm, course);
        boolean saved = this.save(course);
        if(!saved){
            throw new RuntimeException("创建课程失败");
        }

        EduCourseDescription description = new EduCourseDescription();
        description.setDescription(courseInfoForm.getDescription());
        description.setId(course.getId());
        saved = descriptionService.save(description);
        if(!saved){
            throw new RuntimeException("创建课程失败");
        }

        return course.getId();
    }

    @Override
    public CourseInfoForm getCourseInfoById(String id) {
        EduCourse course = this.getById(id);
        EduCourseDescription description = descriptionService.getById(id);
        if (course == null || description == null) {
            throw  new RuntimeException("回显数据失败");
        }

        CourseInfoForm courseInfoForm = new CourseInfoForm();
        BeanUtils.copyProperties(course, courseInfoForm);
        courseInfoForm.setDescription(description.getDescription());
        return courseInfoForm;
    }

    @Override
    public void updateCourseInfo(CourseInfoForm courseInfoForm) {
        EduCourse course = this.getById(courseInfoForm.getId());
        EduCourseDescription description = descriptionService.getById(courseInfoForm.getId());
        BeanUtils.copyProperties(courseInfoForm, course);
        description.setDescription(courseInfoForm.getDescription());
        this.updateById(course);
        descriptionService.updateById(description);
    }

    @Override
    public CoursePublishVo getCoursePublishById(String id) {
        return this.baseMapper.getCoursePublishById(id);
    }

    @Override
    public boolean delCourseInfo(String id) {
        //删视频
        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", id);
        List<EduVideo> videos = videoService.list(videoQueryWrapper);
        String videoSourceIds = StringUtils.join(videos.stream().map(EduVideo::getVideoSourceId).collect(Collectors.toList()), ",");
        Boolean videoSourceRemoved = vodClient.delVideo(videoSourceIds).getSuccess();
        boolean videoRemoved = videoService.removeByIds(videos.stream().map(EduVideo::getId).collect(Collectors.toList()));

        //删章节
        QueryWrapper<EduChapter> chapterQueryWrapper = new QueryWrapper<>();
        chapterQueryWrapper.eq("course_id", id);
        boolean chapterRemoved = chapterService.remove(chapterQueryWrapper);

        //删课程
        boolean descRemoved = descriptionService.removeById(id);
        boolean courseRemoved = this.removeById(id);

        return videoSourceRemoved && videoRemoved && descRemoved && courseRemoved;
    }

    @Override
    public List<EduCourse> selectByTeacherId(String teacherId) {
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();

        queryWrapper.eq("teacher_id", teacherId);
        //按照最后更新时间倒序排列
        queryWrapper.orderByDesc("gmt_modified");

        List<EduCourse> courses = this.list(queryWrapper);
        return courses;
    }

    @Override
    public Map<String, Object> getCourseApiPageVo(Page<EduCourse> pageParam, CourseQueryVo courseQueryVo) {
        String subjectParentId = courseQueryVo.getSubjectParentId();
        String subjectId = courseQueryVo.getSubjectId();
        String buyCountSort = courseQueryVo.getBuyCountSort();
        String gmtCreateSort = courseQueryVo.getGmtCreateSort();
        String priceSort = courseQueryVo.getPriceSort();
        String title = courseQueryVo.getTitle();
        //2 验空，不为空拼写到查询条件
        QueryWrapper<EduCourse> queryWrapper = new QueryWrapper<>();

        if(!StringUtils.isEmpty(subjectParentId)){
            queryWrapper.eq("subject_parent_id",subjectParentId);
        }
        if(!StringUtils.isEmpty(subjectId)){
            queryWrapper.eq("subject_id",subjectId);
        }
        if(!StringUtils.isEmpty(title)){
            queryWrapper.like("title", title);
        }

        if(!StringUtils.isEmpty(buyCountSort)){
            queryWrapper.orderByDesc("buy_count");
        }else{
            if(!StringUtils.isEmpty(gmtCreateSort)){
                queryWrapper.orderByDesc("gmt_create");
            }else{
                if(!StringUtils.isEmpty(priceSort)){
                    queryWrapper.orderByDesc("price");
                }
            }
        }

        queryWrapper.eq("status","Normal");

        this.page(pageParam, queryWrapper);

        List<EduCourse> records = pageParam.getRecords();
        long current = pageParam.getCurrent();
        long pages = pageParam.getPages();
        long size = pageParam.getSize();
        long total = pageParam.getTotal();
        boolean hasNext = pageParam.hasNext();
        boolean hasPrevious = pageParam.hasPrevious();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("items", records);
        map.put("current", current);
        map.put("pages", pages);
        map.put("size", size);
        map.put("total", total);
        map.put("hasNext", hasNext);
        map.put("hasPrevious", hasPrevious);

        return map;
    }

    @Override
    public CourseWebVo getCourseWebVo(String courseId) {
        CourseWebVo courseWebVo = baseMapper.getCourseWebVo(courseId);
        return courseWebVo;
    }
}
