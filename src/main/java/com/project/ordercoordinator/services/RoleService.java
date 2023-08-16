package com.project.ordercoordinator.services;

import com.project.ordercoordinator.models.Account;
import com.project.ordercoordinator.models.Role;
import com.project.ordercoordinator.repositories.AccountRepository;
import com.project.ordercoordinator.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    private AccountRepository accountRepository;

    public List<Role> getAllRole() {
        return roleRepository.findAll();
    }

    public Account getRoleByEmployeeId(Integer employeeId) {
        return accountRepository.findByEmployeeId(employeeId);
    }
}
