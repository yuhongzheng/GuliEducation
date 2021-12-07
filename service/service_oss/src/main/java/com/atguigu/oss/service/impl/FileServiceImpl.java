package com.atguigu.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.atguigu.oss.service.FileService;
import com.atguigu.oss.util.ConstantPropertiesUtil;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {
    @Override
    public String uploadFileOSS(MultipartFile file) {
        String fileName = UUID.randomUUID().toString() + file.getOriginalFilename();
        String path = new DateTime().toString("yyyy/MM/dd");
        fileName = path + "/" + fileName;
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ConstantPropertiesUtil.END_POINT, ConstantPropertiesUtil.ACCESS_KEY_ID,
                ConstantPropertiesUtil.ACCESS_KEY_SECRET);
        try {
            //上传文件流
            InputStream inputStream = file.getInputStream();
            ossClient.putObject(ConstantPropertiesUtil.BUCKET_NAME, fileName, inputStream);
            // 关闭OSSClient。
            ossClient.shutdown();
            //https://guli-file201021.oss-cn-beijing.aliyuncs.com/01.jpg
            return "https://"+ConstantPropertiesUtil.BUCKET_NAME+"."+ConstantPropertiesUtil.END_POINT+"/"+fileName;

        } catch (IOException e) {
            e.printStackTrace();
            throw  new RuntimeException("上传失败");
        }
    }
}
