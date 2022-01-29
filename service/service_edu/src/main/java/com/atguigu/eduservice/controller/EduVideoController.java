package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduVideo;
import com.atguigu.eduservice.entity.vo.VideoInfoForm;
import com.atguigu.eduservice.service.EduVideoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 课程视频 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-12-08
 */
@Api(description = "课时管理")
@RestController
@RequestMapping("/eduservice/edu-video")
@CrossOrigin
public class EduVideoController {
    @Autowired
    private EduVideoService videoService;

    @ApiOperation(value = "新增课时")
    @PostMapping("save")
    public R save(@ApiParam(name = "videoForm", value = "课时对象", required = true)
            @RequestBody VideoInfoForm videoInfoForm) {
        videoService.saveVideoInfo(videoInfoForm);
        return R.ok();
    }

    @ApiOperation(value = "根据ID查询课时")
    @GetMapping("getVideoInfoById/{id}")
    public R getVideoInfoById(
            @ApiParam(name = "id", value = "课时ID", required = true)
            @PathVariable String id){

        VideoInfoForm videoInfoForm = videoService.getVideoInfoFormById(id);
        return R.ok().data("item", videoInfoForm);
    }

    @ApiOperation(value = "更新课时")
    @PutMapping("updateCourseInfo")
    public R updateCourseInfo(
            @ApiParam(name = "VideoInfoForm", value = "课时基本信息", required = true)
            @RequestBody VideoInfoForm videoInfoForm){
        videoService.updateVideoInfoById(videoInfoForm);
        return R.ok();
    }

    @ApiOperation(value = "根据ID删除课时")
    @DeleteMapping("{id}")
    public R removeById(
            @ApiParam(name = "id", value = "课时ID", required = true)
            @PathVariable String id) {

        boolean result = videoService.removeVideoById(id);
        if (result) {
            return R.ok();
        } else {
            return R.error().message("删除失败");
        }
    }
}

