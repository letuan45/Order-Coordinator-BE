package com.project.ordercoordinator.DTO;

import java.util.List;

public class RemainStockDTO {
    private Integer warehouseId;

    private List<RemainStockItemDTO> remainStockItemDTOList;

    public RemainStockDTO() {
    }

    public RemainStockDTO(Integer warehouseId, List<RemainStockItemDTO> remainStockItemDTOList) {
        this.warehouseId = warehouseId;
        this.remainStockItemDTOList = remainStockItemDTOList;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public List<RemainStockItemDTO> getRemainStockItemDTOList() {
        return remainStockItemDTOList;
    }

    public void setRemainStockItemDTOList(List<RemainStockItemDTO> remainStockItemDTOList) {
        this.remainStockItemDTOList = remainStockItemDTOList;
    }

    @Override
    public String toString() {
        return "RemainStockDTO{" +
                "warehouseId=" + warehouseId +
                ", remainStockItemDTOList=" + remainStockItemDTOList +
                '}';
    }
}
