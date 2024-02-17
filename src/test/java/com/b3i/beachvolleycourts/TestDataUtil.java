package com.b3i.beachvolleycourts;

import com.b3i.beachvolleycourts.domains.User;

import java.time.LocalDate;

public class TestDataUtil {
    public static User createTestUserA(){
        return User.builder()
                .id("testUserA")
                .firstName("First name A")
                .lastName("Last name A")
                .email("userA@userA.com")
                .password("passwordA")
                .medicalCertificateExpiryDate(LocalDate.now())
                .membershipExpiryDate(LocalDate.now())
                .isAdmin(false)
                .build();
    }

    public static User createTestUserB(){
        return User.builder()
                .id("testUserB")
                .firstName("First name B")
                .lastName("Last name B")
                .email("userB@userB.com")
                .password("passwordB")
                .medicalCertificateExpiryDate(LocalDate.now())
                .membershipExpiryDate(LocalDate.now())
                .isAdmin(false)
                .build();
    }

    public static User createTestAdmin(){
        return User.builder()
                .id("testAdmin")
                .firstName("First name admin")
                .lastName("Last name admin")
                .email("admin@admin.com")
                .password("passwordAdmin")
                .medicalCertificateExpiryDate(LocalDate.now())
                .membershipExpiryDate(LocalDate.now())
                .isAdmin(true)
                .build();
    }
}

