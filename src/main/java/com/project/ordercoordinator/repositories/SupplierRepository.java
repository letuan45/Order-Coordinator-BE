package com.project.ordercoordinator.repositories;

import com.project.ordercoordinator.models.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Supplier findById(Integer id);
    Supplier findByPhone(String phone);
}
