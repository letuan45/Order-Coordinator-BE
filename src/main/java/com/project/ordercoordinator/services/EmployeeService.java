package com.project.ordercoordinator.services;

import com.project.ordercoordinator.models.Employee;
import com.project.ordercoordinator.models.Product;
import com.project.ordercoordinator.models.Warehouse;
import com.project.ordercoordinator.repositories.EmployeeRepository;
import com.project.ordercoordinator.repositories.WarehouseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private WarehouseRepository warehouseRepository;

    @Autowired
    private AccountService accountService;

    public List<Employee> getAllEmployee() {
        return employeeRepository.findAll();
    }

    public Employee getEmploye(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId);
        if(employee == null) {
            throw new IllegalArgumentException("Không tìm thấy nhân viên");
        }
        return employee;
    }

    public Employee createEmployee(Employee employee, Integer warehouseId) {
        if(warehouseRepository.findById(warehouseId) == null) {
            throw new IllegalArgumentException("Không tìm thấy kho này");
        }
        Warehouse warehouse = warehouseRepository.findById(warehouseId);
        if(warehouse.getActive() == false) {
            throw new IllegalArgumentException("Kho đang không khả dụng!");
        }

        employee.setWarehouse(warehouse);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Employee employee, Integer employeeId) {
        if(employeeRepository.findById(employeeId) == null) {
            throw new IllegalArgumentException("Không tìm thấy nhân viên");
        }
        Employee foundedEmployee = employeeRepository.findById(employeeId);
        foundedEmployee.setFullName(employee.getFullName());
        foundedEmployee.setEmail(employee.getEmail());
        foundedEmployee.setDateOfBirth(employee.getDateOfBirth());
        foundedEmployee.setGender(employee.getGender());
        foundedEmployee.setActive(employee.getActive());

        return employeeRepository.save(foundedEmployee);
    }

    public Page<Employee> getEmployeePaginate(String searchKeyword, Integer page, Integer size) {
        Employee employee = new Employee();
        employee.setFullName(searchKeyword);
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Employee> example = Example.of(employee, matcher);

        Page<Employee> employeesPagination = employeeRepository.findAll(example, PageRequest.of(page, size, Sort.by("id").descending()));
        return employeesPagination;
    }

    public Page<Employee> getEmployeesByWarehouse(Integer warehouseId, Integer page, Integer size) {
        Warehouse warehouse = warehouseRepository.findById(warehouseId);
        if(warehouse == null) {
            throw new IllegalArgumentException("Không tìm thấy kho");
        }

        return employeeRepository.findByWarehouseId(warehouseId, PageRequest.of(page, size));
    }

    // Xóa nhân viên
    // Không cho xóa nhân viên có account
    public String deleteEmployee(Integer employeeId) {
        Boolean employeeHasAccount = accountService.employeeHasAccount(employeeId);
        if(employeeHasAccount) throw new IllegalArgumentException("Không thể xóa nhân viên này!");
        Employee employee = employeeRepository.findById(employeeId);
        employeeRepository.delete(employee);
        return "Xóa nhân viên thành công!";
    }
}
