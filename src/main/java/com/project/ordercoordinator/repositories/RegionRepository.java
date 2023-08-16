package com.project.ordercoordinator.repositories;

import com.project.ordercoordinator.models.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {
    Region findById(Integer id);
}
