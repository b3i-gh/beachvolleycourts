package com.b3i.beachvolleycourts;

import com.b3i.beachvolleycourts.domains.Booking;
import com.b3i.beachvolleycourts.domains.Schedule;
import com.b3i.beachvolleycourts.domains.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestDataUtil {
    public static User createTestUserA() {
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

    public static User createTestUserB() {
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

    public static User createTestAdmin() {
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

    public static Schedule createEmptyTestSchedule() {
        return Schedule.builder()
                .id("emptyTestSchedule")
                .date(LocalDate.now())
                .startTime("16:00:00")
                .endTime("23:00:00")
                .bookings(new ArrayList<>())
                .build();
    }

    public static Schedule createTestScheduleA() {
        return Schedule.builder()
                .id("testScheduleA")
                .date(LocalDate.now())
                .startTime("12:00:00")
                .endTime("20:00:00")
                .bookings(Arrays.asList(createTestBookingA(), createTestBookingB()))
                .build();
    }

    public static Schedule createTestScheduleB() {
        return Schedule.builder()
                .id("testScheduleB")
                .date(LocalDate.now())
                .startTime("13:00:00")
                .endTime("22:00:00")
                .bookings(Arrays.asList(createTestBookingB()))
                .build();
    }

    public static Booking createTestBookingA() {
        return new Booking().builder()
                .bookingId("testBookingA")
                .name("test booking A")
                .startTime("18:00:00")
                .endTime("20:00:00")
                .userId("testUserA")
                .playerList(null) // TODO test user list
                .notes("note test booking A")
                .build();
    }

    public static Booking createTestBookingB() {
        return Booking.builder()
                .bookingId("testBookingB")
                .name("test booking B")
                .startTime("19:00:00")
                .endTime("21:30:00")
                .userId("testUserB")
                .playerList(null) // TODO test user list
                .notes("note test booking B")
                .build();
    }
}

