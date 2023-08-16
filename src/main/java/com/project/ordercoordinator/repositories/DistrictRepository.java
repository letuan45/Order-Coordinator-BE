package com.project.ordercoordinator.repositories;

import com.project.ordercoordinator.models.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DistrictRepository extends JpaRepository<District, Long> {
    District findById(Integer id);

    List<District> findByProvinceId(Integer provinceId);
}
