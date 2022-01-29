package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduCourse;
import com.atguigu.eduservice.entity.vo.CourseInfoForm;
import com.atguigu.eduservice.entity.vo.CoursePublishVo;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduCourseService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
 * @since 2021-12-07
 */
@Api(description="课程管理")
@RestController
@RequestMapping("/eduservice/educourse")
@CrossOrigin
public class EduCourseController {
    @Autowired
    private EduCourseService courseService;

    @ApiOperation(value = "添加课程信息")
    @PostMapping("addCourseInfo")
    public R addCourseInfo(@RequestBody CourseInfoForm courseInfoForm){
        String id = courseService.addCourseInfo(courseInfoForm);
        return R.ok().data("id", id);
    }

    @ApiOperation(value = "根据id课程信息回显数据")
    @GetMapping("getCourseInfoById/{id}")
    public R getCourseInfoById(@PathVariable String id){
        CourseInfoForm courseInfoForm = courseService.getCourseInfoById(id);
        return R.ok().data("courseInfo", courseInfoForm);
    }

    @ApiOperation(value = "修改课程信息")
    @PostMapping("updateCourseInfo")
    public R updateCourseInfo(@RequestBody CourseInfoForm courseInfoForm){
        courseService.updateCourseInfo(courseInfoForm);
        return R.ok();
    }

    @ApiOperation(value = "根据课程id查询课程发布信息")
    @GetMapping("getCoursePublishById/{id}")
    public R getCoursePublishById(@PathVariable String id){
        CoursePublishVo coursePublishVo = courseService.getCoursePublishById(id);
        return R.ok().data("coursePublishVo",coursePublishVo);
    }

    @ApiOperation(value = "根据id发布课程")
    @PutMapping("publishCourse/{id}")
    public R publishCourse(@PathVariable String id){
        EduCourse eduCourse = courseService.getById(id);
        eduCourse.setStatus("Normal");
        courseService.updateById(eduCourse);
        return R.ok();
    }

    @ApiOperation(value = "查询所有课程信息")
    @PostMapping("getCourseInfo/{current}/{limit}")
    //TODO 实现带条件、带分页查询
    public R getCourseInfo(@PathVariable("current") Integer current, @PathVariable("limit") Integer limit){
        Page<EduCourse> page = new Page<>(current, limit);
        courseService.page(page, null);
        return R.ok().data("list",page.getRecords()).data("total", page.getTotal());
    }

    @ApiOperation(value = "根据id删除课程相关信息")
    @DeleteMapping("delCourseInfo/{id}")
    public R delCourseInfo(@PathVariable String id){
        return courseService.delCourseInfo(id)? R.ok(): R.error();
    }

    @ApiOperation(value = "更据教师查询所有课程")
    @GetMapping("selectByTeacherId/{teacherId}")
    public R selectByTeacherId(@PathVariable("teacherId") String teacherId){
        List<EduCourse> courseList = courseService.selectByTeacherId(teacherId);
        return R.ok().data("courseList", courseList);
    }
}

