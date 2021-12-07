package com.atguigu.eduservice;


import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class Test2 {
    @Data
    @NoArgsConstructor
    public static class DemoData{
        @ExcelProperty("学生编号")
        private int sno;
        @ExcelProperty("姓名")
        private String sname;
    }

    static class ExcelListener extends AnalysisEventListener<DemoData>{
        @Override
        public void invoke(DemoData demoData, AnalysisContext analysisContext) {
            System.out.println(demoData);
        }

        @Override
        public void doAfterAllAnalysed(AnalysisContext analysisContext) {

        }
    }

    @Test
    public void test(){
        String path = "D:\\hello\\write.xlsx";
        EasyExcel.write(path, DemoData.class).sheet("学生列表").doWrite(data());
    }

    @Test
    public void test2(){
        String path = "D:\\hello\\write.xlsx";
        EasyExcel.read(path, DemoData.class, new ExcelListener()).sheet().doRead();
    }

    private List<DemoData> data(){
        List<DemoData> list = new ArrayList<DemoData>();
        for (int i = 0; i < 10; i++) {
            DemoData data = new DemoData();
            data.setSno(i);
            data.setSname("张三"+i);
            list.add(data);
        }
        return list;
    }
}
