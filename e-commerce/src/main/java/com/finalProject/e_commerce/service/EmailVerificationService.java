package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.Customer;
import com.finalProject.e_commerce.domain.VerificationToken;
import com.finalProject.e_commerce.dto.customerDTOs.CustomerRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Lazy
@Service
public class EmailVerificationService {


    private final CustomerService customerService;
    private final VerificationTokenService verificationTokenService;
    private final JavaMailSender mailSender;

    @Autowired
    public EmailVerificationService(VerificationTokenService verificationTokenService, JavaMailSender mailSender, @Lazy CustomerService customerService) {
        this.verificationTokenService = verificationTokenService;
        this.mailSender = mailSender;
        this.customerService = customerService;
    }

    public void sendVerificationEmail(String email) {
        Customer customer = customerService.findByEmail(email);
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setCustomer(customer);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(1)); // Token expires in 2 mins
        verificationTokenService.saveToken(verificationToken);

        String subject = "Email Verification";
        String confirmationUrl = "http://localhost:8080/verify?token=" + token;
        String message = "To verify your email address, please click on the following link:\n"
                + confirmationUrl
                + "\nThis verification expires in 1 mins. Click on it even if it's expired, and you will be sent a new one.";

        sendEmail(customer.getEmail(), subject, message);
    }

    public void sendPasswordResetEmail(Customer customer) {
        //N.b check if customer has a token already and delete it

        VerificationToken existingToken = verificationTokenService.findByCustomer(customer);
        if (existingToken != null) {
            // Delete the existing token
            verificationTokenService.deleteToken(existingToken);
        }

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setCustomer(customer);
        verificationToken.setExpiryDate(LocalDateTime.now().plusMinutes(1));
        verificationTokenService.saveToken(verificationToken);

        String subject = "Password Reset Request";
        String resetUrl = "http://localhost:8080/reset-password?token=" + token;
        String message = "Click the link below to reset your password:\n"
                + resetUrl
                + "\nThis verification expires in 1 mins. Click on it even if it's expired, and you will be redirected to enter your mail again.";
        

        sendEmail(customer.getEmail(), subject, message);
    }

    private void sendEmail(String recipientAddress, String subject, String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setFrom("tay.no.reply@gmail.com");
        email.setTo(recipientAddress);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

    public String verifyEmailToken(String token) {
        VerificationToken verificationToken = verificationTokenService.findByTokenName(token);
        if (verificationToken == null) {
            // N.B it will send this if someone enterd any random token that doesn't exist
            // or if the user clicked on the link after verfifying
            return "Invalid token";
        } else if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            verificationTokenService.deleteToken(verificationToken);
            sendVerificationEmail(verificationToken.getCustomer().getEmail());
            return "expired token";
        }
        Customer customer = verificationToken.getCustomer();
        customer.setActive(true);
        customerService.updateCustomer(customer);
        verificationTokenService.deleteToken(verificationToken);
        return "verificationCompleted";
    }

    public String verifyPasswordToken(String token) {
        VerificationToken verificationToken = verificationTokenService.findByTokenName(token);
        if (verificationToken == null) {
            // N.B it will send this if someone enterd any random token that doesn't exist
            // or if the user clicked on the link after verfifying
            return "Invalid token";
        } else if (verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {

            verificationTokenService.deleteToken(verificationToken);
            return "expired token";
        }
        Customer customer = verificationToken.getCustomer();
        customer.setActive(true);
        customerService.updateCustomer(customer);
        verificationTokenService.deleteToken(verificationToken);
        return "verificationCompleted";
    }


}
