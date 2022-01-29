package com.atguigu.eduservice.service.impl;

import com.atguigu.eduservice.client.VodClient;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.VideoInfoForm;
import com.atguigu.eduservice.mapper.EduVideoMapper;
import com.atguigu.eduservice.service.EduVideoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 课程视频 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-12-08
 */
@Service
@Transactional
public class EduVideoServiceImpl extends ServiceImpl<EduVideoMapper, EduVideo> implements EduVideoService {
    @Autowired
    private VodClient vodClient;

    @Override
    public void saveVideoInfo(VideoInfoForm videoInfoForm) {
        EduVideo video = new EduVideo();
        BeanUtils.copyProperties(videoInfoForm, video);
        boolean saved = this.save(video);
        if(!saved){
            throw new RuntimeException("课时信息保存失败");
        }
    }

    @Override
    public VideoInfoForm getVideoInfoFormById(String id) {
        EduVideo video = this.getById(id);
        VideoInfoForm videoInfoForm = new VideoInfoForm();
        BeanUtils.copyProperties(video, videoInfoForm);
        return videoInfoForm;
    }

    @Override
    public void updateVideoInfoById(VideoInfoForm videoInfoForm) {
        EduVideo video = new EduVideo();
        BeanUtils.copyProperties(videoInfoForm, video);
        boolean result = this.updateById(video);
        if(!result){
            throw new RuntimeException("课时信息保存失败");
        }
    }

    @Override
    public boolean removeVideoById(String id) {
        //删除视频资源 TODO
        EduVideo video = this.getById(id);
        if (video == null) {
            return false;
        }

        Boolean success = vodClient.delVideo(video.getVideoSourceId()).getSuccess();
        int result = baseMapper.deleteById(id);
        return result > 0 && success;
    }
}
