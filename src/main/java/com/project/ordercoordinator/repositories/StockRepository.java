package com.project.ordercoordinator.repositories;

import com.project.ordercoordinator.keys.StockId;
import com.project.ordercoordinator.models.Stock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends JpaRepository<Stock, StockId> {
}
