package com.atguigu.orderservice.client;

import com.atguigu.commonutils.vo.UcenterMemberForOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Component
@FeignClient("service-ucenter")
public interface UcenterClient {
    @GetMapping("/ucenterservice/member/getUcenterForOrder/{memberId}")
    public UcenterMemberForOrder getUcenterForOrder(@PathVariable("memberId") String memberId);
}
