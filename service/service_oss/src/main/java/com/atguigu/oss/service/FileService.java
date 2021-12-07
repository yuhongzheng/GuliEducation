package com.atguigu.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    public String uploadFileOSS(MultipartFile file);
}
