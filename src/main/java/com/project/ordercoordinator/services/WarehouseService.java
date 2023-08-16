package com.project.ordercoordinator.services;

import com.project.ordercoordinator.exceptions.ResourceNotFoundException;
import com.project.ordercoordinator.models.*;
import com.project.ordercoordinator.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WarehouseService {
    @Autowired
    WarehouseRepository warehouseRepository;

    @Autowired
    private DistrictRepository districtRepository;

    @Autowired
    private ReceiptRepository receiptRepository;

    public List<Warehouse> getAllWarehouse() {
        return warehouseRepository.findAll();
    }

    public Warehouse createWarehouse(Warehouse warehouse, Integer districtId) {
        District district = districtRepository.findById(districtId);
        if(district == null) {
            throw new IllegalArgumentException("Địa chỉ không hợp lệ");
        }

        warehouse.setDistrict(district);
        return warehouseRepository.save(warehouse);
    }

    public Warehouse updateWarehouse(Warehouse warehouse,
                                     Integer warehouseId) {
        //Chưa làm update địa chỉ (Chỉ cho phép update khi chưa có nhân viên nào và giao dịch nào)
        if(warehouseRepository.findById(warehouseId) == null) {
            throw new IllegalArgumentException("Kho không hợp lệ");
        }
        if(warehouseRepository.findById(warehouseId).getActive() == false) {
            throw new IllegalArgumentException("Kho này không thể sửa");
        }

        Warehouse newWarehouse =  warehouseRepository.findById(warehouseId);

        newWarehouse.setName(warehouse.getName());
        newWarehouse.setAdditionAddress(warehouse.getAdditionAddress());
        newWarehouse.setActive(warehouse.getActive());

        return warehouseRepository.save(newWarehouse);
    }

    public Warehouse getWarehouse(Integer warehouseId) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId);
        if(warehouse == null) {
            throw new IllegalArgumentException("Không tìm thấy kho");
        }
        return warehouse;
    }

    public Page<Warehouse> getWHWithPagination(String searchKeyword, Integer offset, Integer pageSize) {
        Warehouse warehouse = new Warehouse();
        warehouse.setName(searchKeyword);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING)
                .withIgnorePaths("isActive");
        Example<Warehouse> example = Example.of(warehouse, matcher);

        Page<Warehouse> warehouses = warehouseRepository.findAll(example,
                PageRequest.of(offset, pageSize, Sort.by("id").descending()));
        return warehouses;
    }

    public String deleteWarehouse(Integer warehouseId) {
        List<Receipt> receiptList = receiptRepository.findAll();
        for(Receipt receiptItem : receiptList) {
            if(receiptItem.getWarehouse().getId() == warehouseId) {
                throw new IllegalArgumentException("Xóa thất bại! kho đã có giao dịch!");
            }
        }
        Warehouse warehouse = warehouseRepository.findById(warehouseId);
        warehouseRepository.delete(warehouse);
        return "Xóa kho thành công";
    }
}
