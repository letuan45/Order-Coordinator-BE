package com.project.ordercoordinator.keys;
import com.project.ordercoordinator.models.Product;
import com.project.ordercoordinator.models.Receipt;

import javax.persistence.*;
import java.io.Serializable;

@Embeddable
public class ReceiptDetailId implements Serializable {
    @ManyToOne(targetEntity = Receipt.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "receipt_id")
    private Receipt receipt;

    @ManyToOne(targetEntity = Product.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id")
    private Product product;

    public ReceiptDetailId() {
    }

    public ReceiptDetailId(Receipt receipt, Product product) {
        this.receipt = receipt;
        this.product = product;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "ReceiptDetailId{" +
                "receipt=" + receipt +
                ", product=" + product +
                '}';
    }
}
