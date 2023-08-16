package com.project.ordercoordinator.controllers;

import com.project.ordercoordinator.models.Supplier;
import com.project.ordercoordinator.services.SupplierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    @GetMapping("/get-all")
    public ResponseEntity<List<Supplier>> getAllSupplier() {
        return ResponseEntity.ok(supplierService.getAllSupplier());
    }

    @PostMapping("/create")
    public ResponseEntity<Supplier> createSupplier(@RequestBody Supplier supplier) {
        return ResponseEntity.ok(supplierService.createSupplier(supplier));
    }

    @PutMapping("/update/{supplierId}")
    public ResponseEntity<Supplier> updateSupplier(@RequestBody Supplier supplier, @PathVariable Integer supplierId) {
        return ResponseEntity.ok(supplierService.updateSupplier(supplier, supplierId));
    }

    @GetMapping("/get/{supplierId}")
    public ResponseEntity<Supplier> getSupplier(@PathVariable Integer supplierId) {
        return ResponseEntity.ok(supplierService.getSupplier(supplierId));
    }

    @GetMapping("/get/paginate")
    public ResponseEntity<Page<Supplier>> getSupplierPagination(@RequestParam String s,
                                                                @RequestParam Integer p,
                                                                @RequestParam Integer size) {
        Page<Supplier> supplierPagination = supplierService.getSupplierWithPaginate(s, p, size);
        return ResponseEntity.ok(supplierPagination);
    }

    @DeleteMapping("/delete/{supplierId}")
    public ResponseEntity<String> deleteSupplier(@PathVariable Integer supplierId) {
        return ResponseEntity.ok(supplierService.deleteSupplier(supplierId));
    }
}
