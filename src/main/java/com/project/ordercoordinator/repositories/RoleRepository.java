package com.project.ordercoordinator.repositories;

import com.project.ordercoordinator.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findById(Integer id);
}
