package com.project.ordercoordinator.repositories;

import com.project.ordercoordinator.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findById(Integer productId);
}
