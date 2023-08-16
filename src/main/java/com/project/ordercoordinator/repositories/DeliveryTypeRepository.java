package com.project.ordercoordinator.repositories;

import com.project.ordercoordinator.models.DeliveryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryTypeRepository extends JpaRepository<DeliveryType, Integer> {
}
