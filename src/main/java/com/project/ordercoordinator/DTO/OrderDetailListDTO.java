package com.project.ordercoordinator.DTO;

import java.util.List;

public class OrderDetailListDTO {
    private List<OrderDetailDTO> orderDetails;

    public OrderDetailListDTO() {
    }

    public OrderDetailListDTO(List<OrderDetailDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public List<OrderDetailDTO> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetailDTO> orderDetails) {
        this.orderDetails = orderDetails;
    }

    @Override
    public String toString() {
        return "OrderDetailListDTO{" +
                "orderDetails=" + orderDetails +
                '}';
    }
}
