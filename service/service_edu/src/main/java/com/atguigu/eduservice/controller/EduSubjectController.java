package com.atguigu.eduservice.controller;


import com.atguigu.commonutils.R;
import com.atguigu.eduservice.entity.vo.OneSubjectVo;
import com.atguigu.eduservice.service.EduSubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * <p>
 * 课程科目 前端控制器
 * </p>
 *
 * @author testjava
 * @since 2021-12-07
 */
@RestController
@RequestMapping("/eduservice/edu-subject")
@Api(description = "课程分类管理")
@CrossOrigin
public class EduSubjectController {
    @Autowired
    private EduSubjectService subjectService;

    @ApiOperation(value = "批量导入课程分类")
    @PostMapping("addSubject")
    public R addSubject(MultipartFile file){
        subjectService.addSubject(file);
        return R.ok();
    }

    @ApiOperation(value = "查询所有课程分类")
    @GetMapping("getAllSubject")
    public R getAllSubject(){
        List<OneSubjectVo> allSubject = subjectService.getAllSubject();
        return R.ok().data("allSubject", allSubject);
    }
}

