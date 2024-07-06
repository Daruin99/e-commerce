package com.finalProject.e_commerce.service;

import com.finalProject.e_commerce.domain.VerificationToken;
import com.finalProject.e_commerce.repository.VerificationTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VerificationTokenService {

    private VerificationTokenRepo verificationTokenRepo;

    @Autowired
    public VerificationTokenService(VerificationTokenRepo verificationTokenRepo) {
        this.verificationTokenRepo = verificationTokenRepo;
    }

    public void saveToken(VerificationToken verificationToken) {
         verificationTokenRepo.save(verificationToken);
    }

    public void deleteToken(VerificationToken verificationToken) {
        verificationTokenRepo.delete(verificationToken);
    }

    public VerificationToken findByTokenName(String tokenName) {
        return verificationTokenRepo.findByToken(tokenName);
    }
}
