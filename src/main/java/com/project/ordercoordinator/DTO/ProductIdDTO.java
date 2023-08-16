package com.project.ordercoordinator.DTO;

import java.util.List;

public class ProductIdDTO {
    private List<Integer> productIdList;

    public ProductIdDTO() {
    }

    public ProductIdDTO(List<Integer> productIdList) {
        this.productIdList = productIdList;
    }

    public List<Integer> getProductIdList() {
        return productIdList;
    }

    public void setProductIdList(List<Integer> productIdList) {
        this.productIdList = productIdList;
    }

    @Override
    public String toString() {
        return "ProductIdDTO{" +
                "productIdList=" + productIdList +
                '}';
    }
}
