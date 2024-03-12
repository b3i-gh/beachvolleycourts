package com.b3i.beachvolleycourts.controllers;

import com.b3i.beachvolleycourts.TestDataUtil;
import com.b3i.beachvolleycourts.domains.Booking;
import com.b3i.beachvolleycourts.domains.Schedule;
import com.b3i.beachvolleycourts.services.BookingService;
import com.b3i.beachvolleycourts.services.ScheduleService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookingControllerTests {
    private ScheduleService scheduleService;
    private BookingService bookingService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private MongoTemplate mongoTemplate;

    @Autowired
    public BookingControllerTests(ScheduleService scheduleService, MockMvc mockMvc, MongoTemplate mongoTemplate, BookingService bookingService) {
        this.scheduleService = scheduleService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        this.mongoTemplate = mongoTemplate;
        this.bookingService = bookingService;
    }

    @AfterEach
    public void cleanUp() {
        mongoTemplate.remove(new Query(), "bookings");
    }

    @Test
    public void findBookingByIdShouldReturnTheBookingAndOKHttpResponseIfTheBookingExists() throws Exception {
        Schedule testScheduleA = TestDataUtil.createTestScheduleA();
        Booking testBookingA = TestDataUtil.createTestBookingA();
        testScheduleA.setBookings(Arrays.asList(testBookingA));
        scheduleService.save(testScheduleA);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/bookings/" + testBookingA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(testBookingA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testBookingA.getName())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void findBookingByIdShouldReturnNOTFOUNDHttpResponseIfTheBookingDoesntExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/bookings/notExistingId")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void findBookingsByScheduleIdShouldReturnTheBookingListAndOKHttpResponseIfTheScheduleExists() throws Exception {
        Schedule testScheduleA = TestDataUtil.createTestScheduleA();
        Booking testBookingA = TestDataUtil.createTestBookingA();
        Booking testBookingB = TestDataUtil.createTestBookingB();
        testScheduleA.setBookings(Arrays.asList(testBookingA, testBookingB));
        scheduleService.save(testScheduleA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/schedules/" + testScheduleA.getId() + "/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").value(testBookingA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].id").value(testBookingB.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void findBookingsByScheduleIdShouldReturnNOTFOUNDHttpResponseIfTheScheduleDoesntExist() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.get("/bookings/notExistingId")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void saveShouldReturnTheCreatedBookingCREATEDHttpResponse() throws Exception {
        Schedule testScheduleA = TestDataUtil.createTestScheduleA();
        scheduleService.save(testScheduleA);
        Booking testBookingA = TestDataUtil.createTestBookingA();
        bookingService.save(testScheduleA.getId(), testBookingA);

        String scheduleJson = objectMapper.writeValueAsString(testScheduleA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson)
        );

        String bookingJson = objectMapper.writeValueAsString(testBookingA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules/" + testScheduleA.getId() + "/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(testBookingA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testBookingA.getName())
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void saveShouldReturnNOTFOUNDHttpResponseIfScheduleDoesntExists() throws Exception {
        Schedule testScheduleA = TestDataUtil.createTestScheduleA();
        scheduleService.save(testScheduleA);

        Booking testBookingA = TestDataUtil.createTestBookingA();
        bookingService.save(testScheduleA.getId(), testBookingA);

        String bookingJson = objectMapper.writeValueAsString(testBookingA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules/notExistingId/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void saveShouldReturnNOTACCEPTABLEResponseIfStartTimeIsBeforeScheduleStartTime() throws Exception {
        Schedule testScheduleA = TestDataUtil.createTestScheduleA();
        testScheduleA.setDate(LocalDate.now().plusDays(2));
        testScheduleA.setStartTime("10:00:00");
        testScheduleA.setEndTime("14:00:00");
        scheduleService.save(testScheduleA);

        Booking testBookingA = TestDataUtil.createTestBookingA();
        testBookingA.setStartTime("09:00:00");
        testBookingA.setStartTime("13:00:00");
        bookingService.save(testScheduleA.getId(), testBookingA);


        String scheduleJson = objectMapper.writeValueAsString(testScheduleA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson)
        );

        String bookingJson = objectMapper.writeValueAsString(testBookingA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules/" + testScheduleA.getId() + "/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotAcceptable()
        );
    }

    @Test
    public void saveShouldReturnNOTACCEPTABLEResponseIfEndTimeIsAfterScheduleEndTime() throws Exception {
        Schedule testScheduleA = TestDataUtil.createTestScheduleA();
        testScheduleA.setDate(LocalDate.now().plusDays(2));
        testScheduleA.setStartTime("10:00:00");
        testScheduleA.setEndTime("14:00:00");
        scheduleService.save(testScheduleA);

        Booking testBookingA = TestDataUtil.createTestBookingA();
        testBookingA.setStartTime("10:30:00");
        testBookingA.setStartTime("18:00:00");
        bookingService.save(testScheduleA.getId(), testBookingA);


        String scheduleJson = objectMapper.writeValueAsString(testScheduleA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson)
        );

        String bookingJson = objectMapper.writeValueAsString(testBookingA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules/" + testScheduleA.getId() + "/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotAcceptable()
        );
    }

}