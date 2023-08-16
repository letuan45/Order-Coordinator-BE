package com.project.ordercoordinator.services;

import com.project.ordercoordinator.models.Customer;
import com.project.ordercoordinator.models.Order;
import com.project.ordercoordinator.models.Product;
import com.project.ordercoordinator.repositories.CustomerRepository;
import com.project.ordercoordinator.repositories.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private OrderRepository orderRepository;

    public Page<Customer> getCustomers(String searchKeyword , Integer page, Integer size) {
        Customer customer = new Customer();
        customer.setName(searchKeyword);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Customer> example = Example.of(customer, matcher);

        return customerRepository.findAll(example, PageRequest.of(page, size, Sort.by("id").descending()));
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Integer customerId, Customer customerBodyData) {
        Optional<Customer> customer = customerRepository.findById(customerId);
        if(customer == null) {
            throw new IllegalArgumentException("Không tìm thấy khách hàng");
        }
        Customer customerData = customer.get();
        customerData.setEmail(customerBodyData.getEmail());
        customerData.setName(customerBodyData.getName());
        customerData.setPhone(customerBodyData.getPhone());
        return customerRepository.save(customerData);
    }

    public Customer getCustomerById(Integer customerId) {
        Optional<Customer> customerRaw = customerRepository.findById(customerId);
        if(customerRaw == null) {
            throw new IllegalArgumentException("Không tìm thấy khách hàng");
        }
        Customer customer = customerRaw.get();
        return customer;
    }

    public String deleteCustomer(Integer customerId) {
        List<Order> orderList = orderRepository.findAll();
        for(Order orderItem : orderList) {
            if(orderItem.getCustomer().getId() == customerId) {
                throw new IllegalArgumentException("Xóa thất bại! Khách hàng đã có giao dịch!");
            }
        }
        customerRepository.deleteById(customerId);
        return "Xóa khách hàng thành công!";
    }
}
