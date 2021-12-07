package com.atguigu.eduservice.service.impl;

import com.alibaba.excel.EasyExcel;
import com.atguigu.eduservice.entity.EduSubject;
import com.atguigu.eduservice.entity.vo.ExcelSubjectData;
import com.atguigu.eduservice.entity.vo.OneSubjectVo;
import com.atguigu.eduservice.entity.vo.TwoSubjectVo;
import com.atguigu.eduservice.listener.SubjectExcelListener;
import com.atguigu.eduservice.mapper.EduSubjectMapper;
import com.atguigu.eduservice.service.EduSubjectService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 课程科目 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-12-07
 */
@Service
public class EduSubjectServiceImpl extends ServiceImpl<EduSubjectMapper, EduSubject> implements EduSubjectService {
    @Override
    public void addSubject(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), ExcelSubjectData.class, new SubjectExcelListener(this))
                    .sheet().doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<OneSubjectVo> getAllSubject() {
        QueryWrapper<EduSubject> wrapper = new QueryWrapper<>();
        wrapper.eq("parent_id", "0");
        List<EduSubject> ones = this.list(wrapper);
        wrapper = new QueryWrapper<>();
        wrapper.ne("parent_id", "0");
        List<EduSubject> twos = this.list(wrapper);

        List<OneSubjectVo> result = new ArrayList<>(ones.size());
        for (EduSubject one : ones) {
            OneSubjectVo oneSubjectVo = new OneSubjectVo();
            BeanUtils.copyProperties(one, oneSubjectVo);
            for (EduSubject two : twos) {
                if(two.getParentId().equals(one.getId())){
                    TwoSubjectVo twoSubjectVo = new TwoSubjectVo();
                    BeanUtils.copyProperties(two, twoSubjectVo);
                    oneSubjectVo.getChildren().add(twoSubjectVo);
                }
            }
            result.add(oneSubjectVo);
        }

        return result;
    }
}
