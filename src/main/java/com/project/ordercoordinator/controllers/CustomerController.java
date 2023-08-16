package com.project.ordercoordinator.controllers;

import com.project.ordercoordinator.models.Customer;
import com.project.ordercoordinator.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/api/customer")
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @GetMapping("/get")
    public ResponseEntity<Page<Customer>> getCustomer(@RequestParam String s, @RequestParam Integer p, @RequestParam Integer size) {
        return ResponseEntity.ok(customerService.getCustomers(s, p, size));
    }

    @PostMapping("/create")
    public ResponseEntity<Customer> createCustomer(@RequestBody Customer customer) {
        return ResponseEntity.ok(customerService.createCustomer(customer));
    }

    @PutMapping("/update/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer, @PathVariable Integer customerId) {
        return ResponseEntity.ok(customerService.updateCustomer(customerId, customer));
    }

    @GetMapping("/get-by-id/{customerId}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable Integer customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @DeleteMapping("/delete/{customerId}")
    public ResponseEntity<String> deleteCustomer(@PathVariable Integer customerId) {
        return ResponseEntity.ok(customerService.deleteCustomer(customerId));
    }
}
