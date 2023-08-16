package com.project.ordercoordinator.controllers;

import com.project.ordercoordinator.models.OrderDetail;
import com.project.ordercoordinator.repositories.OrderDetailRepository;
import com.project.ordercoordinator.services.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/api/order-detail")
public class OrderDetailController {
    @Autowired
    private OrderDetailService orderDetailService;

    @GetMapping("/get-by-id/{orderId}")
    public ResponseEntity<List<OrderDetail>> getReceiptDetailsByReceipt(@PathVariable Integer orderId) {
        return ResponseEntity.ok(orderDetailService.getByOrderId(orderId));
    }
}
