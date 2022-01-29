package com.atguigu.vod.util;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.profile.DefaultProfile;

public class AliyunVodSDKUtils {
    public static final String ACCESSKEYID = "LTAI5tQPRNwjVneZpVfuLbwe";
    public static final String SECRET = "Vt4BI6Dg5PebOHF5xTyU7Ju78h9oEJ";

    public static DefaultAcsClient initVodClient() throws ClientException {
        String regionId = "cn-shanghai";  // 点播服务接入区域
        DefaultProfile profile = DefaultProfile.getProfile(regionId, ACCESSKEYID, SECRET);
        DefaultAcsClient client = new DefaultAcsClient(profile);
        return client;
    }
}
