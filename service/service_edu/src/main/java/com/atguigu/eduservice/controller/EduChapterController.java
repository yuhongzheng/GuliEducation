package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduChapter;
import com.atguigu.eduservice.entity.vo.ChapterVo;
import com.atguigu.eduservice.service.EduChapterService;
import com.atguigu.eduservice.service.EduCourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 课程 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-12-08
 */
@RestController
@RequestMapping("/eduservice/edu-chapter")
@Api(description="章节管理")
@CrossOrigin
public class EduChapterController {
    @Autowired
    private EduChapterService chapterService;
    @Autowired
    private EduCourseService courseService;

    @ApiOperation(value = "根据课程id查询章节、小节信息")
    @GetMapping("getChapterVideoById/{courseId}")
    public R getChapterVideoById(@PathVariable String courseId){
        List<ChapterVo> chapterVoList = chapterService.getChapterVideoById(courseId);
        String status = courseService.getById(courseId).getStatus();
        return R.ok().data("chapterVideoList",chapterVoList).data("status", status);
    }

    @ApiOperation(value = "添加章节")
    @PostMapping("addChapter")
    public R addChapter(@RequestBody EduChapter eduChapter){
        chapterService.save(eduChapter);
        return R.ok();
    }

    @ApiOperation(value = "根据id删除章节")
    @DeleteMapping("delChapter/{id}")
    public R delChapter(@PathVariable String id){
        return chapterService.remove(id)? R.ok(): R.error();
    }

    @ApiOperation(value = "根据id查询章节")
    @GetMapping("getChapterById/{id}")
    public R getChapterById(@PathVariable String id){
        EduChapter eduChapter = chapterService.getById(id);
        return R.ok().data("eduChapter",eduChapter);
    }
    @ApiOperation(value = "修改章节")
    @PostMapping("updateChapter")
    public R updateChapter(@RequestBody EduChapter eduChapter){
        chapterService.updateById(eduChapter);
        return R.ok();
    }
}

