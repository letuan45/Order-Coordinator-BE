package com.project.ordercoordinator.controllers;

import com.project.ordercoordinator.models.Employee;
import com.project.ordercoordinator.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/get-all")
    public ResponseEntity<List<Employee>> getAllEmployee() {
        return ResponseEntity.ok(employeeService.getAllEmployee());
    }

    @GetMapping("/get/{employeeId}")
    public ResponseEntity<Employee> getEmployee(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(employeeService.getEmploye(employeeId));
    }

    @PostMapping("/create")
    public ResponseEntity<Employee> createEmployee(
            @RequestBody Employee employee,
            @RequestParam Integer warehouseId) {
        return ResponseEntity.ok(employeeService.createEmployee(employee, warehouseId));
    }

    @PutMapping("/update/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(
            @RequestBody Employee employee,
            @PathVariable Integer employeeId
    ) {
        return ResponseEntity.ok(employeeService.updateEmployee(employee, employeeId));
    }

    @GetMapping("/get/paginate")
    public ResponseEntity<Page<Employee>> getEmployeePaginate(
            @RequestParam String s,
            @RequestParam Integer p,
            @RequestParam Integer size) {
        return ResponseEntity.ok(employeeService.getEmployeePaginate(s, p, size));
    }

    @GetMapping("/get-by-warehouse/{warehouseId}")
    public ResponseEntity<Page<Employee>> GetEmployeesByWarehouse(
            @PathVariable Integer warehouseId,
            @RequestParam Integer p,
            @RequestParam Integer size
    ) {
        return ResponseEntity.ok(employeeService.getEmployeesByWarehouse(warehouseId, p, size));
    }

    @DeleteMapping("/delete/{employeeId}")
    public ResponseEntity<String> deleteEmployee(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(employeeService.deleteEmployee(employeeId));
    }
}
