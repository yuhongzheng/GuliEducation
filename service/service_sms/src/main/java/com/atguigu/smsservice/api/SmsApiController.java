package com.atguigu.smsservice.api;

import com.atguigu.commonutils.R;
import com.atguigu.smsservice.service.SmsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Api(description="短信发送")
@RestController
@RequestMapping("/edusms/sms")
@CrossOrigin
public class SmsApiController {
    @Autowired
    private SmsService smsService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @ApiOperation(value = "短信发送")
    @GetMapping("sendSmsPhone/{phone}")
    public R  sendSmsPhone(@PathVariable String phone){
        try {
            smsService.sendSmsPhone(phone);
            return R.ok();
        }catch (Exception e){
            return R.error().message(e.getMessage());
        }
    }


}
