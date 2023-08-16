package com.project.ordercoordinator.DTO;

public class RemainStockItemDTO {
    private Integer productId;

    private Integer quantity;

    public RemainStockItemDTO() {
    }

    public RemainStockItemDTO(Integer productId, Integer quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "RemainStockItemDTO{" +
                "productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
