package com.b3i.beachvolleycourts;

import com.b3i.beachvolleycourts.domains.Booking;
import com.b3i.beachvolleycourts.domains.Schedule;
import com.b3i.beachvolleycourts.domains.User;

import java.time.LocalDate;
import java.util.List;

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

    public static Schedule createTestScheduleA(){
        Booking testBookingA = createTestBookingA();
        Booking testBookingB = createTestBookingB();
        List<Booking> testBookings = List.of(testBookingA, testBookingB);

        return Schedule.builder()
                .id("testScheduleA")
                .date(LocalDate.now())
                .startTime("16:00:00")
                .endTime("23:00:00")
                .bookings(testBookings)
                .build();
    }

    public static Schedule createTestScheduleB(){
        Booking testBookingA = createTestBookingA();
        Booking testBookingB = createTestBookingB();
        List<Booking> testBookings = List.of(testBookingA, testBookingB);

        return Schedule.builder()
                .id("testScheduleB")
                .date(LocalDate.now().minusDays(1))
                .startTime("17:00:00")
                .endTime("22:30:00")
                .bookings(testBookings)
                .build();
    }

    public static Booking createTestBookingA() {
        return Booking.builder()
                .id("testBookingA")
                .name("test booking A")
                .startTime("18:00:00")
                .endTime("20:00:00")
                .userId("testUserA")
                .playerList(null) // TODO test user list
                .isApproved(true)
                .notes("note test booking A")
                .build();
    }

    public static Booking createTestBookingB() {
        return Booking.builder()
                .id("testBookingB")
                .name("test booking B")
                .startTime("19:00:00")
                .endTime("21:30:00")
                .userId("testUserB")
                .playerList(null) // TODO test user list
                .isApproved(true)
                .notes("note test booking B")
                .build();
    }
}

