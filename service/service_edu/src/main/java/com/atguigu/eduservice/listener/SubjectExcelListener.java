package com.atguigu.eduservice.listener;


import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.ExcelSubjectData;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SubjectExcelListener extends AnalysisEventListener<ExcelSubjectData> {
    private EduSubjectService eduSubjectService;

    private EduSubject existOneSubject(String title){
        return this.existTwoSubject(title, "0");
    }

    private EduSubject existTwoSubject(String title, String pid){
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("title", title);
        wrapper.eq("parent_id", pid);
        return eduSubjectService.getOne(wrapper);
    }

    @Override
    public void invoke(ExcelSubjectData excelSubjectData, AnalysisContext analysisContext) {
        if (excelSubjectData == null) {
            throw new RuntimeException("导入课程分类信息失败");
        }

        EduSubject parent = this.existOneSubject(excelSubjectData.getOneSubjectName());
        if (parent == null) {
            parent = new EduSubject();
            parent.setParentId("0");
            parent.setTitle(excelSubjectData.getOneSubjectName());
            eduSubjectService.save(parent);
        }

        EduSubject child = this.existTwoSubject(excelSubjectData.getTwoSubjectName(), parent.getId());
        if (child == null) {
            child = new EduSubject();
            child.setParentId(parent.getId());
            child.setTitle(excelSubjectData.getTwoSubjectName());
            eduSubjectService.save(child);
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {

    }
}
