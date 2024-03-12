package com.b3i.beachvolleycourts.controllers;

import com.b3i.beachvolleycourts.TestDataUtil;
import com.b3i.beachvolleycourts.domains.Booking;
import com.b3i.beachvolleycourts.domains.Schedule;
import com.b3i.beachvolleycourts.services.ScheduleService;
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
import java.util.ArrayList;
import java.util.Arrays;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class BookingControllerTests {
    private ScheduleService scheduleService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private MongoTemplate mongoTemplate;

    @Autowired
    public BookingControllerTests(ScheduleService scheduleService, MockMvc mockMvc, MongoTemplate mongoTemplate) {
        this.scheduleService = scheduleService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        this.mongoTemplate = mongoTemplate;
    }

    @AfterEach
    public void cleanUp() {
        mongoTemplate.remove(new Query(), "bookings");
    }

    @Test
    public void findBookingsByScheduleIdShouldReturnTheBookingListAndOKHttpResponseIfTheScheduleExists() throws Exception{
        Schedule testScheduleA = TestDataUtil.createTestScheduleA();
        Booking testBookingA = TestDataUtil.createTestBookingA();
        Booking testBookingB = TestDataUtil.createTestBookingB();
        testScheduleA.setBookings(Arrays.asList(testBookingA, testBookingB));
        scheduleService.save(testScheduleA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/bookings/" + testScheduleA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(testScheduleA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].bookings").value(testBookingA)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].bookings").value(testBookingB)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void findBookingsByScheduleIdShouldReturnNOTFOUNDHttpResponseIfTheScheduleDoesntExist() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/bookings/notExistingId")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }




//
//    @Test
//    public void createScheduleShouldSaveScheduleAndReturnCREATEDHttpResponse() throws Exception {
//        Schedule testScheduleA = TestDataUtil.createTestScheduleA();
//        String scheduleJson = objectMapper.writeValueAsString(testScheduleA);
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/schedules")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(scheduleJson)
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.id").isString()
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.date").value(testScheduleA.getDate().toString())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.startTime").value(testScheduleA.getStartTime())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.endTime").value(testScheduleA.getEndTime())
//        ).andExpect(
//                MockMvcResultMatchers.status().isCreated()
//        );
//    }
//

//

//
//    @Test
//    public void fullUpdateScheduleShouldUpdateTheSchedulesAndReturnOKHttpResponse() throws Exception{
//        Schedule testScheduleA = TestDataUtil.createTestScheduleA();
//        scheduleService.save(testScheduleA);
//
//        Schedule updatedSchedule = TestDataUtil.createTestScheduleA();
//        updatedSchedule.setId(testScheduleA.getId());
//        updatedSchedule.setDate(LocalDate.now().plusDays(2));
//        updatedSchedule.setStartTime("11:00:00");
//        updatedSchedule.setEndTime("17:30:00");
//
//        String scheduleJson = objectMapper.writeValueAsString(updatedSchedule);
//        mockMvc.perform(
//                MockMvcRequestBuilders.put("/schedules/" + testScheduleA.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(scheduleJson)
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.id").isString()
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.date").value(updatedSchedule.getDate().toString())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.startTime").value(updatedSchedule.getStartTime())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.endTime").value(updatedSchedule.getEndTime())
//        ).andExpect(
//                MockMvcResultMatchers.status().isOk()
//        );
//    }
//
//    @Test
//    public void fullUpdateScheduleShouldReturnNOTFOUNDHttpResponseIfTheScheduleDoesntExist() throws Exception {
//        Schedule testScheduleA = TestDataUtil.createTestScheduleA();
//        String scheduleJson = objectMapper.writeValueAsString(testScheduleA);
//        mockMvc.perform(
//                MockMvcRequestBuilders.put("/schedules/notExistingId")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(scheduleJson)
//        ).andExpect(
//                MockMvcResultMatchers.status().isNotFound()
//        );
//    }
//
//    @Test
//    public void partialUpdateScheduleShouldUpdateTheSchedulesAndReturnOKHttpResponse() throws Exception{
//        Schedule testScheduleA = TestDataUtil.createTestScheduleA();
//        scheduleService.save(testScheduleA);
//
//        Schedule updatedSchedule = TestDataUtil.createTestScheduleA();
//        updatedSchedule.setEndTime("21:34:56");
//        String scheduleJson = objectMapper.writeValueAsString(updatedSchedule);
//
//        mockMvc.perform(
//                MockMvcRequestBuilders.patch("/schedules/" + testScheduleA.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(scheduleJson)
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.id").isString()
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.date").value(testScheduleA.getDate().toString())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.startTime").value(testScheduleA.getStartTime())
//        ).andExpect(
//                MockMvcResultMatchers.jsonPath("$.endTime").value(updatedSchedule.getEndTime())
//        ).andExpect(
//                MockMvcResultMatchers.status().isOk()
//        );
//    }
//
//    @Test
//    public void partialUpdateScheduleShouldReturnNOTFOUNDHttpResponseIfTheScheduleDoesntExist() throws Exception{
//        Schedule testScheduleA = TestDataUtil.createTestScheduleA();
//        String scheduleJson = objectMapper.writeValueAsString(testScheduleA);
//        mockMvc.perform(
//                MockMvcRequestBuilders.patch("/schedules/notExistingId")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(scheduleJson)
//        ).andExpect(
//                MockMvcResultMatchers.status().isNotFound()
//        );
//    }
//
//    @Test
//    public void deleteShouldDeleteTheScheduleAndReturnNOCONTENTHttpResponse() throws Exception{
//        Schedule testScheduleA = TestDataUtil.createTestScheduleA();
//        scheduleService.save(testScheduleA);
//        mockMvc.perform(
//                MockMvcRequestBuilders.delete("/schedules/" + testScheduleA.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//        ).andExpect(
//                MockMvcResultMatchers.status().isNoContent()
//        );
//    }
//
//    @Test
//    public void createScheduleWithEndTimeBeforeStartTimeShouldReturnNOTACCEPTABLEHttpResponse() throws Exception{
//        Schedule testScheduleA = TestDataUtil.createTestScheduleA();
//        testScheduleA.setStartTime("17:00:00");
//        testScheduleA.setEndTime("16:00:00");
//        String scheduleJson = objectMapper.writeValueAsString(testScheduleA);
//        mockMvc.perform(
//                MockMvcRequestBuilders.post("/schedules")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(scheduleJson)
//        ).andExpect(
//                MockMvcResultMatchers.status().isNotAcceptable()
//        );
//    }
}
