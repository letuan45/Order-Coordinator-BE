package com.project.ordercoordinator.services;

import com.project.ordercoordinator.models.Account;
import com.project.ordercoordinator.models.Employee;
import com.project.ordercoordinator.models.Role;
import com.project.ordercoordinator.repositories.AccountRepository;
import com.project.ordercoordinator.repositories.EmployeeRepository;
import com.project.ordercoordinator.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public Account register(Account account, Integer employeeId, Integer roleId) {
        if(employeeRepository.findById(employeeId) == null ||
                employeeRepository.findById(employeeId).getActive() == false) {
            throw new IllegalArgumentException("Nhân viên không hợp lệ");
        }
        if(roleRepository.findById(roleId) == null) {
            throw new IllegalArgumentException("Quyền user không hợp lệ");
        }
        if(accountRepository.findByUsername(account.getUsername()) != null) {
            throw new IllegalArgumentException("Tên tài khoản đã tồn tại");
        }

        Employee employee = employeeRepository.findById(employeeId);

        List<Account> allAccount = accountRepository.findAll();
        if(allAccount != null && allAccount.size() > 0) {
            for (Account accountItem : allAccount) {
                if(accountItem.getEmployee().getId() == employeeId) {
                    throw new IllegalArgumentException("Nhân viên này đã có tài khoản");
                }
            }
        }

        Role role = roleRepository.findById(roleId);
        String encodedPassword = passwordEncoder.encode(account.getPassword());

        account.setEmployee(employee);
        account.setRole(role);
        account.setPassword(encodedPassword);

        return accountRepository.save(account);
    }

    public Account login(String username, String password) {
        Account account = accountRepository.findByUsername(username);

        // Kiểm tra xem tài khoản có tồn tại và mật khẩu khớp hay không
        if (account == null || !passwordEncoder.matches(password, account.getPassword())) {
            throw new IllegalArgumentException("Thông tin không đúng, vui lòng kiểm tra lại");
        }
        if(account.getActive() == false) {
            throw new IllegalArgumentException("Tài khoản này đang tạm khóa");
        }

        return account;
    }

    public Account changeAccountInformation(String oldPassword,
                                            String newPassword,
                                            Integer employeeId) {
        Account foundedAccount = accountRepository.findByEmployeeId(employeeId);
        if(foundedAccount == null) {
            throw new IllegalArgumentException("Nhân viên không có tài khoản!");
        }
        if(!passwordEncoder.matches(oldPassword, foundedAccount.getPassword())) {
            throw new IllegalArgumentException("Mật khẩu cũ không đúng, vui lòng kiểm tra lại");
        }
        foundedAccount.setPassword(passwordEncoder.encode(newPassword));
        return accountRepository.save(foundedAccount);
    }

    public Account getAcountbyEmployee(Integer employeeId) {
        return accountRepository.findByEmployeeId(employeeId);
    }

    public Account activeAccount(Integer accountId) {
        Account account = accountRepository.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Tài khoản không tồn tại");
        }
        account.setActive(true);
        return accountRepository.save(account);
    }

    public Account disableAccount(Integer accountId) {
        Account account = accountRepository.findById(accountId);
        if (account == null) {
            throw new IllegalArgumentException("Tài khoản không tồn tại");
        }
        account.setActive(false);
        return accountRepository.save(account);
    }

    //Check 1 nhân viên có tài khoản chưa
    public Boolean employeeHasAccount(Integer employeeId) {
        List<Account> accountList = accountRepository.findAll();
        for(Account accountItem : accountList) {
            if(accountItem.getEmployee().getId() == employeeId) return true;
        }
        return false;
    }

    public Account grantAccessRole(Integer employeeId, Integer roleId) {
        //Get account by employee
        Account account = accountRepository.findByEmployeeId(employeeId);
        Role role = roleRepository.findById(roleId);
        if(account == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản nhân viên!");
        }
        if(role == null) {
            throw new IllegalArgumentException("Không tìm thấy quyền của tài khoản");
        }
        account.setRole(role);
        return accountRepository.save(account);
    }

    public static String maskEmail(String email) {
        int atIndex = email.indexOf('@');
        int domainStart = atIndex + 1;
        int lastDotIndex = email.lastIndexOf('.');

        StringBuilder maskedEmailBuilder = new StringBuilder();

        // Mask characters before '@', except the first and last characters
        maskedEmailBuilder.append(email.charAt(0));
        maskedEmailBuilder.append("********");
        maskedEmailBuilder.append(email.charAt(atIndex - 1));

        // Append '@' character
        maskedEmailBuilder.append('@');

        // Append the rest of the email
        for (int i = domainStart; i < email.length(); i++) {
            maskedEmailBuilder.append(email.charAt(i));
        }

        return maskedEmailBuilder.toString();
    }

    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        javaMailSender.send(message);
    }

    // Quên mật khẩu
    public String requestPasswordReset(String username) {
        Account account = accountRepository.findByUsername(username);
        if (account != null) {
            String resetToken = generateResetToken();
            account.setResetToken(resetToken);
            accountRepository.save(account);

            sendResetTokenByEmail(account.getEmployee().getEmail(), resetToken);
            return "Một email đã gửi đến hòm thư của bạn: " + maskEmail(account.getEmployee().getEmail());
        } else {
            throw new IllegalArgumentException("Không tìm thấy tài khoản! Hãy kiểm tra lại");
        }
    }

    public Account resetTokenValidation(String resetToken) {
        Account account = accountRepository.findByResetToken(resetToken);
        if(account == null) {
            throw new IllegalArgumentException("Mã khôi phục không đúng!");
        }
        return account;
    }

    public String resetPassword(String resetToken, String newPassword) {
        Account account = resetTokenValidation(resetToken);
        if (account != null) {
            account.setPassword(passwordEncoder.encode(newPassword));
            account.setResetToken(null);
            accountRepository.save(account);
        }
        return "Đổi mật khẩu thành công!";
    }

    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    private void sendResetTokenByEmail(String email, String resetToken) {
        String subject = "OC System - Khôi phục tài khoản";
        String content = "Xin chào bạn, đây là email tự động từ hệ thống chúng tôi\n" +
                "Mã khôi phục tài khoản của bạn là: " + resetToken;

        sendEmail(email, subject, content);
    }
}
