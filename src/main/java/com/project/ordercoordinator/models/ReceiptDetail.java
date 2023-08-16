package com.project.ordercoordinator.models;

import com.project.ordercoordinator.keys.ReceiptDetailId;

import javax.persistence.*;

@Entity
@Table(name = "receipt_detail")
public class ReceiptDetail {
    @Column(name = "amount", nullable = false)
    private Integer amount;

    @EmbeddedId
    private ReceiptDetailId id;

    public ReceiptDetail() {
    }

    public ReceiptDetail(Integer amount, ReceiptDetailId id) {
        this.amount = amount;
        this.id = id;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public ReceiptDetailId getId() {
        return id;
    }

    public void setId(ReceiptDetailId id) {
        this.id = id;
    }
}
