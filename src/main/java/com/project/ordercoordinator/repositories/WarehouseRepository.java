package com.project.ordercoordinator.repositories;

import com.project.ordercoordinator.models.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Warehouse findById(Integer id);
}
