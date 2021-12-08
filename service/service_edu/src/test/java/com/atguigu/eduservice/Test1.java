package com.atguigu.eduservice;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.service.EduSubjectService;
import com.atguigu.eduservice.service.EduTeacherService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Test1 {
    @Autowired
    EduTeacherService service;
    @Autowired
    EduSubjectService eduSubjectService;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SubjectDemo {
        @ExcelProperty(value = "一级课程名称", index = 0)
        private String oneName;
        @ExcelProperty(value = "二级课程名称", index = 1)
        private String twoName;
    }

    @Test
    public void test() {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", 0);
        List<EduSubject> parentList = eduSubjectService.list(wrapper);
        wrapper = new QueryWrapper<>();
        wrapper.ne("parent_id", 0);
        List<EduSubject> children = eduSubjectService.list(wrapper);
        List<SubjectDemo> demoList = new ArrayList<>(children.size());
        for (EduSubject child : children) {
            String oneName = parentList.stream().filter(parent -> parent.getId().equals(child.getParentId()))
                    .map(EduSubject::getTitle).findFirst().get();
            demoList.add(new SubjectDemo(oneName, child.getTitle()));
        }
        EasyExcel.write("D:\\hello\\subject.xlsx", SubjectDemo.class).sheet("课程信息").doWrite(demoList);
    }
}