package com.project.ordercoordinator.DTO;

public class OrderDetailDTO {
    private Integer productId;

    private Integer amount;

    public OrderDetailDTO() {
    }

    public OrderDetailDTO(Integer productId, Integer amount) {
        this.productId = productId;
        this.amount = amount;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OrderDetailDTO{" +
                "productId=" + productId +
                ", amount=" + amount +
                '}';
    }
}
