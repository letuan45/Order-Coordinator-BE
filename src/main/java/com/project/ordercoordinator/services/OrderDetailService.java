package com.project.ordercoordinator.services;

import com.project.ordercoordinator.models.Order;
import com.project.ordercoordinator.models.OrderDetail;
import com.project.ordercoordinator.repositories.OrderDetailRepository;
import com.project.ordercoordinator.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {
    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private OrderRepository orderRepository;

    public List<OrderDetail> getByOrderId(Integer orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if(order == null) {
            throw new IllegalArgumentException("Không tìm thấy đơn hàng");
        }
        List<OrderDetail> orderDetails = orderDetailRepository.findAll();
        List<OrderDetail> result = new ArrayList<OrderDetail>();

        for(OrderDetail item : orderDetails) {
            if(item.getId().getOrder() == order.get()) {
                result.add(item);
            }
        }

        return result;
    }
}
