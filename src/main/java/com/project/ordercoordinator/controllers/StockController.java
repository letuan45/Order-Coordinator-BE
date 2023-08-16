package com.project.ordercoordinator.controllers;

import com.project.ordercoordinator.models.Stock;
import com.project.ordercoordinator.services.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stock")
public class StockController {
    @Autowired
    private StockService stockService;

    @GetMapping("/get-stocks/{warehouseId}")
    public ResponseEntity<List<Stock>> getStocksByWarehouse(@PathVariable Integer warehouseId) {
        return ResponseEntity.ok(stockService.getStocksByWarehouse(warehouseId));
    }
}
