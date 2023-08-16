package com.project.ordercoordinator.repositories;

import com.project.ordercoordinator.keys.OrderDetailId;
import com.project.ordercoordinator.models.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail,OrderDetailId> {
}
