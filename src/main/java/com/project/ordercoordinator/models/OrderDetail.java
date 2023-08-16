package com.project.ordercoordinator.models;

import com.project.ordercoordinator.keys.OrderDetailId;

import javax.persistence.*;

@Entity
@Table(name = "order_detail")
public class OrderDetail {
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @EmbeddedId
    private OrderDetailId id;

    public OrderDetail() {
    }

    public OrderDetail(Integer amount, OrderDetailId id) {
        this.amount = amount;
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public OrderDetailId getId() {
        return id;
    }

    public void setId(OrderDetailId id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "amount=" + amount +
                ", id=" + id +
                '}';
    }
}

