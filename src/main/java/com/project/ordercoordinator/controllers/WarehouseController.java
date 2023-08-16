package com.project.ordercoordinator.controllers;

import com.project.ordercoordinator.models.Warehouse;
import com.project.ordercoordinator.services.WarehouseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/warehouse/")
public class WarehouseController {
    @Autowired
    private WarehouseService warehouseService;

    @GetMapping("/get-all")
    public List<Warehouse> getAllWareHouse() {
        return warehouseService.getAllWarehouse();
    }

    @GetMapping("/get-warehouse/{warehouseId}")
    public ResponseEntity<Warehouse> getWarehouseById(@PathVariable Integer warehouseId) {
        return ResponseEntity.ok(warehouseService.getWarehouse(warehouseId));
    }

    @PostMapping("/create")
    public ResponseEntity<Warehouse> createWarehouse(
        @RequestBody Warehouse warehouse,
        @RequestParam Integer districtId
    ) {
        return ResponseEntity.ok(warehouseService.createWarehouse(warehouse, districtId));
    }

    @PutMapping("/update/{warehouseId}")
    public ResponseEntity<Warehouse> updateWareHouse(
        @RequestBody Warehouse warehouse,
        @PathVariable Integer warehouseId
    ) {
        return ResponseEntity.ok(warehouseService.updateWarehouse(warehouse, warehouseId));
    }

    @GetMapping("/get/paginate")
    private ResponseEntity<Page<Warehouse>> getWarehousePagination(
            @RequestParam String s,
            @RequestParam Integer p,
            @RequestParam Integer size) {
        Page<Warehouse> warehousesPagination = warehouseService.getWHWithPagination(s, p, size);
        return ResponseEntity.ok(warehousesPagination);
    }

    @DeleteMapping("/delete/{warehouseId}")
    private ResponseEntity<String> deleteWarehouse(@PathVariable Integer warehouseId) {
        return ResponseEntity.ok(warehouseService.deleteWarehouse(warehouseId));
    }
}
