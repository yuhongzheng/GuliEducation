package com.atguigu.orderservice.service;

import com.atguigu.orderservice.entity.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 订单 服务类
 * </p>
 *
 * @author testjava
 * @since 2021-12-12
 */
public interface TOrderService extends IService<TOrder> {

    String createOrder(String courseId, String memberId);
}
