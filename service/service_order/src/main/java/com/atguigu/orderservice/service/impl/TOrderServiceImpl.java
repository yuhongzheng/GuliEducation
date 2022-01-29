package com.atguigu.orderservice.service.impl;

import com.atguigu.commonutils.vo.CourseWebVoForOrder;
import com.atguigu.commonutils.vo.UcenterMemberForOrder;
import com.atguigu.orderservice.client.EduClient;
import com.atguigu.orderservice.client.UcenterClient;
import com.atguigu.orderservice.entity.TOrder;
import com.atguigu.orderservice.mapper.TOrderMapper;
import com.atguigu.orderservice.service.TOrderService;
import com.atguigu.orderservice.utils.OrderNoUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 订单 服务实现类
 * </p>
 *
 * @author testjava
 * @since 2021-12-12
 */
@Service
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {
    @Autowired
    EduClient eduClient;
    @Autowired
    UcenterClient ucenterClient;

    @Override
    public String createOrder(String courseId, String memberId) {
        CourseWebVoForOrder courseInfo = eduClient.getCourseInfoForOrder(courseId);
        if (courseInfo == null) {
            throw new RuntimeException("获取课程信息失败");
        }
        UcenterMemberForOrder ucenterMember = ucenterClient.getUcenterForOrder(memberId);
        if (ucenterMember == null) {
            throw new RuntimeException("获取用户信息失败");
        }

        TOrder tOrder = new TOrder(
                null, OrderNoUtil.getOrderNo(), courseInfo.getId(), courseInfo.getTitle(),
                courseInfo.getCover(), courseInfo.getTeacherName(), memberId, ucenterMember.getNickname(),
                ucenterMember.getMobile(), courseInfo.getPrice(), 1, 0,
                null, null, null
        );

        this.save(tOrder);
        return tOrder.getOrderNo();
    }
}
