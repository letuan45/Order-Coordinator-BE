package com.project.ordercoordinator.repositories;

import com.project.ordercoordinator.models.Receipt;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Integer> {
    Page<Receipt> findByDateAfter(Date startDate, Pageable pageable);

    Page<Receipt> findByDateBefore(Date endDate, Pageable pageable);

    Page<Receipt> findByDateBetween(Date startDate, Date endDate, Pageable pageable);
}
