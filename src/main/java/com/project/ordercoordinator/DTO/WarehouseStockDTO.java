package com.project.ordercoordinator.DTO;

import com.project.ordercoordinator.models.Warehouse;

public class WarehouseStockDTO {
    private Warehouse warehouse;

    private Integer stockLength;

    private Integer typeStockLength;

    private Integer receipstLength;

    private Integer ordersLength;

    public WarehouseStockDTO() {
    }

    public WarehouseStockDTO(Warehouse warehouse, Integer stockLength, Integer typeStockLength, Integer receipstLength, Integer ordersLength) {
        this.warehouse = warehouse;
        this.stockLength = stockLength;
        this.typeStockLength = typeStockLength;
        this.receipstLength = receipstLength;
        this.ordersLength = ordersLength;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Integer getStockLength() {
        return stockLength;
    }

    public void setStockLength(Integer stockLength) {
        this.stockLength = stockLength;
    }

    public Integer getTypeStockLength() {
        return typeStockLength;
    }

    public void setTypeStockLength(Integer typeStockLength) {
        this.typeStockLength = typeStockLength;
    }

    public Integer getReceipstLength() {
        return receipstLength;
    }

    public void setReceipstLength(Integer receipstLength) {
        this.receipstLength = receipstLength;
    }

    public Integer getOrdersLength() {
        return ordersLength;
    }

    public void setOrdersLength(Integer ordersLength) {
        this.ordersLength = ordersLength;
    }

    @Override
    public String toString() {
        return "WarehouseStockDTO{" +
                "warehouse=" + warehouse +
                ", stockLength=" + stockLength +
                ", typeStockLength=" + typeStockLength +
                ", receipstLength=" + receipstLength +
                ", ordersLength=" + ordersLength +
                '}';
    }
}
