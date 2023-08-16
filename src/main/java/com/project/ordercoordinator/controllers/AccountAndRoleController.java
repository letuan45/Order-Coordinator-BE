package com.project.ordercoordinator.controllers;

import com.project.ordercoordinator.models.Account;
import com.project.ordercoordinator.models.Role;
import com.project.ordercoordinator.repositories.RoleRepository;
import com.project.ordercoordinator.services.AccountService;
import com.project.ordercoordinator.services.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountAndRoleController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private RoleService roleService;

    @GetMapping("/role/get-all")
    private ResponseEntity<List<Role>> getAllRole()  {
        return ResponseEntity.ok(roleService.getAllRole());
    }

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account,
                            @RequestParam Integer employeeId,
                            @RequestParam Integer roleId) {
        return ResponseEntity.ok(accountService.register(account, employeeId, roleId));
    }

    @PostMapping("/login")
    public Account login(@RequestParam String username, @RequestParam String password) {
        return accountService.login(username, password);
    }

    @PutMapping("/change")
    public ResponseEntity<Account> updateAccount(
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam Integer employeeId) {
        return ResponseEntity.ok(accountService.changeAccountInformation(oldPassword, newPassword, employeeId));
    }

    @GetMapping("/active/{accountId}")
    public ResponseEntity<Account> activeAcount(@PathVariable Integer accountId) {
        return ResponseEntity.ok((accountService.activeAccount(accountId)));
    }

    @GetMapping("/disable/{accountId}")
    public ResponseEntity<Account> disableAcount(@PathVariable Integer accountId) {
        return ResponseEntity.ok((accountService.disableAccount(accountId)));
    }

    @GetMapping("/has-account/{employeeId}")
    public Boolean checkEmployeeHasAccount(@PathVariable Integer employeeId) {
        return accountService.employeeHasAccount(employeeId);
    }

    @GetMapping("/get-role/{employeeId}")
    public ResponseEntity<Role> getRoleByEmployeeId(@PathVariable Integer employeeId) {
        return ResponseEntity.ok(accountService.getAcountbyEmployee(employeeId).getRole());
    }

    @PutMapping("/grant")
    public ResponseEntity<Account> grantUserAccount(
            @RequestParam Integer employeeId,
            @RequestParam Integer roleId) {
        return ResponseEntity.ok(accountService.grantAccessRole(employeeId, roleId));
    }

    @PostMapping("/forget")
    public ResponseEntity<String> resetPassRequest(@RequestParam String username) {
        return ResponseEntity.ok(accountService.requestPasswordReset(username));
    }

    @PostMapping("/forget/change-password")
    public ResponseEntity<String> resetPassword(@RequestParam String resetToken, @RequestParam String newPassword) {
        return ResponseEntity.ok(accountService.resetPassword(resetToken, newPassword));
    }
}
