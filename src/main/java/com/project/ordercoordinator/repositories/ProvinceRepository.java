package com.project.ordercoordinator.repositories;

import com.project.ordercoordinator.models.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProvinceRepository extends JpaRepository<Province, Long> {
    Province findById(Integer id);

    List<Province> findByRegionId(Integer regionId);
}
