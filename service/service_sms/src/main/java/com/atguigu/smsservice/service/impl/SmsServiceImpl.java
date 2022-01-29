package com.atguigu.smsservice.service.impl;

import com.atguigu.commonutils.utils.RandomUtil;
import com.atguigu.smsservice.service.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.TimeUnit;

@Service
public class SmsServiceImpl implements SmsService {
    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Override
    public void sendSmsPhone(String phone) {
        if (StringUtils.isEmpty(phone)) {
            throw new RuntimeException("Null phone.");
        }

        String times = redisTemplate.opsForValue().get(RandomUtil.PREFIX_TIMES + phone);
        if (times != null && Integer.parseInt(times) >= 3) {
            throw new RuntimeException("超过三次发送验证码");
        }

        String code = RandomUtil.getFourBitRandom();
        System.out.println("phone =" + phone + ", code = " + code);
        redisTemplate.opsForValue().set(RandomUtil.PREFIX_PHONE + phone, code, 5, TimeUnit.MINUTES);
        if(times == null){
            redisTemplate.opsForValue().set(RandomUtil.PREFIX_TIMES + phone, "1", 1, TimeUnit.DAYS);
        }else {
            redisTemplate.opsForValue().increment(RandomUtil.PREFIX_TIMES + phone);
        }
    }
}
