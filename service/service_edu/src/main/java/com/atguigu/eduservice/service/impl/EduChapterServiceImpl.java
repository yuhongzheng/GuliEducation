package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.entity.vo.VideoVo;
import com.atguigu.eduservice.mapper.EduChapterMapper;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 课程 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-12-08
 */
@Service
public class EduChapterServiceImpl extends ServiceImpl<EduChapterMapper, EduChapter> implements EduChapterService {
    @Autowired
    private EduVideoService videoService;
    @Autowired
    private VodClient vodClient;

    @Override
    public List<ChapterVo> getChapterVideoById(String courseId) {
        QueryWrapper<EduChapter> wrapper = new QueryWrapper<>();
        wrapper.eq("course_id", courseId);
        List<EduChapter> chapters = this.list(wrapper);

        QueryWrapper<EduVideo> videoQueryWrapper = new QueryWrapper<>();
        videoQueryWrapper.eq("course_id", courseId);
        List<EduVideo> videos = videoService.list(videoQueryWrapper);
        List<ChapterVo> result = new ArrayList<>(chapters.size());
        for (EduChapter chapter : chapters) {
            ChapterVo chapterVo = new ChapterVo();
            BeanUtils.copyProperties(chapter, chapterVo);
            for (EduVideo video : videos) {
                if(video.getChapterId().equals(chapter.getId())){
                    VideoVo videoVo = new VideoVo();
                    BeanUtils.copyProperties(video, videoVo);
                    chapterVo.getChildren().add(videoVo);
                }
            }

            result.add(chapterVo);
        }

        return result;
    }

    @Override
    public boolean remove(String id) {
        QueryWrapper<EduVideo> videoWrapper = new QueryWrapper<>();
        videoWrapper.eq("chapter_id", id);
        List<EduVideo> videos = videoService.list(videoWrapper);
        //删除视频文件
        String sourceIds = StringUtils.join(videos.stream().map(EduVideo::getVideoSourceId).collect(Collectors.toList()), ",");
        Boolean success = vodClient.delVideo(sourceIds).getSuccess();
        //删除视频记录
        boolean videoRemoved = videoService.removeByIds(videos.stream().map(EduVideo::getId).collect(Collectors.toList()));
        //删除章节
        boolean removed = this.removeById(id);

        return success && removed && videoRemoved;
    }
}
