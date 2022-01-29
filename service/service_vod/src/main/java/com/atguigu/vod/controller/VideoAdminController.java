package com.atguigu.vod.controller;

import com.aliyun.vod.upload.impl.UploadVideoImpl;
import com.aliyun.vod.upload.req.UploadStreamRequest;
import com.aliyun.vod.upload.resp.UploadStreamResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.vod.model.v20170321.DeleteVideoRequest;
import com.aliyuncs.vod.model.v20170321.DeleteVideoResponse;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthRequest;
import com.aliyuncs.vod.model.v20170321.GetVideoPlayAuthResponse;
import com.atguigu.commonutils.R;
import com.atguigu.vod.util.AliyunVodSDKUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@CrossOrigin
@Api(description="视频管理")
@RestController
@RequestMapping("/eduvod/video")
public class VideoAdminController {
    @ApiOperation(value = "上传视频")
    @PostMapping("uploadVideo")
    public R uploadVideo(MultipartFile file) throws IOException {
        InputStream inputStream = file.getInputStream();
        String filename = file.getOriginalFilename();
        String title = filename.substring(0, filename.lastIndexOf('.'));
        UploadStreamRequest request = new UploadStreamRequest(AliyunVodSDKUtils.ACCESSKEYID, AliyunVodSDKUtils.SECRET,
                title, filename, inputStream);
        UploadVideoImpl uploader = new UploadVideoImpl();
        UploadStreamResponse response = uploader.uploadStream(request);
        String videoId = response.getVideoId();
        return R.ok().data("videoId", videoId);
    }

    @ApiOperation(value = "删除视频")
    @DeleteMapping("delVideo/{videoId}")
    public R delVideo(@PathVariable("videoId") String videoId){
        try {
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient();
            DeleteVideoRequest request = new DeleteVideoRequest();
            request.setVideoIds(videoId);
            DeleteVideoResponse response = client.getAcsResponse(request);
            return R.ok();
        }catch (ClientException e){
            throw new RuntimeException(e);
        }
    }

    @ApiOperation(value = "根据视频id获取视频播放凭证")
    @GetMapping("getPlayAuth/{vid}")
    public R getPlayAuth(@PathVariable String vid){
        try {
            DefaultAcsClient client = AliyunVodSDKUtils.initVodClient();
            GetVideoPlayAuthRequest request = new GetVideoPlayAuthRequest();
            request.setVideoId(vid);
            GetVideoPlayAuthResponse resp = client.getAcsResponse(request);
            return R.ok().data("playAuth", resp.getPlayAuth());
        } catch (ClientException e) {
            throw new RuntimeException("获取playAuth失败");
        }
    }

}
