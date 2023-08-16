package com.project.ordercoordinator.repositories;

import com.project.ordercoordinator.keys.ReceiptDetailId;
import com.project.ordercoordinator.models.ReceiptDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptDetailRepository extends JpaRepository<ReceiptDetail, ReceiptDetailId> {
}
