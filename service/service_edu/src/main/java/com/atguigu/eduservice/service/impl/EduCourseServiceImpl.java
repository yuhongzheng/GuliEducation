package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.EduCourseDescription;
import com.atguigu.eduservice.entity.vo.CourseInfoForm;
import com.atguigu.eduservice.mapper.EduCourseMapper;
import com.atguigu.eduservice.service.EduCourseDescriptionService;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
}
