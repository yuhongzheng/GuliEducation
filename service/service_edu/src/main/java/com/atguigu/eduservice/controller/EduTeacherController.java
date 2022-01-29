package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.EduTeacher;
import com.atguigu.eduservice.entity.vo.TeacherQuery;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 讲师 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-12-03
 */
@RestController
@Api(description = "讲师信息")
@RequestMapping("/eduservice/edu-teacher")
@CrossOrigin
public class EduTeacherController {
    @Autowired
    private EduTeacherService teacherService;


    @GetMapping
    @ApiOperation(value = "所有讲师列表")
    public R getAllTeacher() {
        List<EduTeacher> list = teacherService.list(null);
        return R.ok().data("list", list);
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "删除讲师")
    public R delTeacher(@PathVariable("id") String id) {
        boolean result = teacherService.removeById(id);
        return result ? R.ok() : R.error();
    }

    @PostMapping("/getTeacherPage/{current}/{limit}")
    @ApiOperation("讲师分页")
    public R getTeacherPage(@PathVariable("current") Integer current, @PathVariable("limit") Integer limit,
                            @RequestBody TeacherQuery teacherQuery) {
        String name = teacherQuery.getName();
        Integer level = teacherQuery.getLevel();
        String begin = teacherQuery.getBegin();
        String end = teacherQuery.getEnd();
        QueryWrapper<EduTeacher> wrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(name)) {
            wrapper.like("name", name);
        }
        if (!StringUtils.isEmpty(level)) {
            wrapper.eq("level", level);
        }
        if (!StringUtils.isEmpty(begin)) {
            wrapper.ge("gmt_create", begin);
        }
        if (!StringUtils.isEmpty(end)) {
            wrapper.le("gmt_create", end);
        }

        Page<EduTeacher> page = new Page<>(current, limit);
        teacherService.page(page, wrapper);
        return R.ok().data("list", page.getRecords()).data("total", page.getTotal());
    }

    @PostMapping("addTeacher")
    @ApiOperation("添加讲师")
    public R addTeacher(@RequestBody EduTeacher eduTeacher){
        return this.teacherService.save(eduTeacher) ? R.ok() : R.error();
    }

    @ApiOperation(value = "根据id查询讲师")
    @GetMapping("getTeacherById/{id}")
    public R getTeacherById(@PathVariable String id){
        EduTeacher eduTeacher = teacherService.getById(id);
        return R.ok().data("eduTeacher",eduTeacher);
    }

    @PostMapping("updateTeacher")
    @ApiOperation("更新讲师")
    public R updateTeacher(@RequestBody EduTeacher eduTeacher){
        return this.teacherService.updateById(eduTeacher) ? R.ok() : R.error();
    }

    @ApiOperation(value = "分页讲师列表")
    @GetMapping(value = "{page}/{limit}")
    public R pageList(
            @ApiParam(name = "page", value = "当前页码", required = true)
            @PathVariable Long page,
            @ApiParam(name = "limit", value = "每页记录数", required = true)
            @PathVariable Long limit){

        Page<EduTeacher> pageParam = new Page<>(page, limit);

        Map<String, Object> map = teacherService.pageListWeb(pageParam);

        return  R.ok().data(map);
    }
}

