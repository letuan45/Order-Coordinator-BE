package com.project.ordercoordinator.repositories;

import com.project.ordercoordinator.models.DeliveryPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryPriceRepository extends JpaRepository<DeliveryPrice, Integer> {
}
