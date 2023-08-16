package com.project.ordercoordinator.repositories;

import com.project.ordercoordinator.models.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, Long> {
    Partner findById(Integer id);
}
