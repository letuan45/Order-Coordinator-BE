package com.project.ordercoordinator.repositories;

import com.project.ordercoordinator.models.Employee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Employee findById(Integer id);
    Page<Employee> findByWarehouseId(Integer warehouseId, Pageable pageable);
}
