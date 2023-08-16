package com.project.ordercoordinator.repositories;
import com.project.ordercoordinator.models.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findByDateAfter(Date startDate, Pageable pageable);

    Page<Order> findByDateBefore(Date endDate, Pageable pageable);

    Page<Order> findByDateBetween(Date startDate, Date endDate, Pageable pageable);
}
