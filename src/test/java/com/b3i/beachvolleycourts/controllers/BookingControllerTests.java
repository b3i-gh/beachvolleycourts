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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
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
        mongoTemplate.remove(new Query(), "schedules");
    }

    @Test
    public void findBookingByIdShouldReturnTheBookingAndOKHttpResponseIfTheBookingExists() throws Exception {
        Schedule testScheduleA = TestDataUtil.createTestScheduleA();
        Booking testBookingA = TestDataUtil.createTestBookingA();
        testScheduleA.setBookings(Arrays.asList(testBookingA));
        scheduleService.save(testScheduleA);
        mockMvc.perform(
                MockMvcRequestBuilders.get("/bookings/" + testBookingA.getBookingId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.bookingId").value(testBookingA.getBookingId())
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
                MockMvcResultMatchers.jsonPath("$[0].bookingId").value(testBookingA.getBookingId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].bookingId").value(testBookingB.getBookingId())
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
    public void createBookingShouldReturnTheCreatedBookingCREATEDHttpResponse() throws Exception {
        Schedule emptyTestSchedule = TestDataUtil.createEmptyTestSchedule();
        scheduleService.save(emptyTestSchedule);
        Booking testBookingA = TestDataUtil.createTestBookingA();

        String scheduleJson = objectMapper.writeValueAsString(emptyTestSchedule);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson)
        );

        String bookingJson = objectMapper.writeValueAsString(testBookingA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules/" + emptyTestSchedule.getId() + "/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value(testBookingA.getName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.approved").value(false)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.scheduleId").value(emptyTestSchedule.getId())
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void createBookingShouldReturnNOTFOUNDHttpResponseIfScheduleDoesntExists() throws Exception {
        Schedule emptyTestSchedule = TestDataUtil.createEmptyTestSchedule();
        scheduleService.save(emptyTestSchedule);

        Booking testBookingA = TestDataUtil.createTestBookingA();
        bookingService.createBooking(emptyTestSchedule.getId(), testBookingA);

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
    public void createBookingShouldReturnNOTACCEPTABLEResponseIfStartTimeIsBeforeScheduleStartTime() throws Exception {
        Schedule emptyScheduleA = TestDataUtil.createEmptyTestSchedule();
        emptyScheduleA.setDate(LocalDate.now().plusDays(2));
        emptyScheduleA.setStartTime("10:00:00");
        emptyScheduleA.setEndTime("14:00:00");
        scheduleService.save(emptyScheduleA);

        Booking testBookingA = TestDataUtil.createTestBookingA();
        testBookingA.setStartTime("09:00:00");
        testBookingA.setStartTime("13:00:00");
        bookingService.createBooking(emptyScheduleA.getId(), testBookingA);


        String scheduleJson = objectMapper.writeValueAsString(emptyScheduleA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson)
        );

        String bookingJson = objectMapper.writeValueAsString(testBookingA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules/" + emptyScheduleA.getId() + "/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotAcceptable()
        );
    }

    @Test
    public void createBookingShouldReturnNOTACCEPTABLEResponseIfEndTimeIsAfterScheduleEndTime() throws Exception {
        Schedule emptyScheduleA = TestDataUtil.createEmptyTestSchedule();
        emptyScheduleA.setDate(LocalDate.now().plusDays(2));
        emptyScheduleA.setStartTime("10:00:00");
        emptyScheduleA.setEndTime("14:00:00");
        scheduleService.save(emptyScheduleA);

        Booking testBookingA = TestDataUtil.createTestBookingA();
        testBookingA.setStartTime("10:30:00");
        testBookingA.setStartTime("18:00:00");
        bookingService.createBooking(emptyScheduleA.getId(), testBookingA);


        String scheduleJson = objectMapper.writeValueAsString(emptyScheduleA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson)
        );

        String bookingJson = objectMapper.writeValueAsString(testBookingA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules/" + emptyScheduleA.getId() + "/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotAcceptable()
        );
    }

    @Test
    public void updateBookingShouldReturnUpdatedBookingAndOKHttpResponseIfBookingExists() throws Exception {
        Schedule emptyTestSchedule = TestDataUtil.createEmptyTestSchedule();
        scheduleService.save(emptyTestSchedule);
        Booking testBookingA = TestDataUtil.createTestBookingA();

        String scheduleJson = objectMapper.writeValueAsString(emptyTestSchedule);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson)
        );

        String bookingJson = objectMapper.writeValueAsString(testBookingA);
        MvcResult res = mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules/" + emptyTestSchedule.getId() + "/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson)
        ).andReturn();
        Booking updatedBooking = objectMapper.readValue(res.getResponse().getContentAsString(), Booking.class);

        updatedBooking.setName("UPDATED");
        updatedBooking.setScheduleId(emptyTestSchedule.getId());
        String updatedBookingJson = objectMapper.writeValueAsString(updatedBooking);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/bookings/" + updatedBooking.getBookingId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookingJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.name").value("UPDATED")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void updateBookingShouldReturnNOTFOUNDHttpResponseIfBookingDoesntExists() throws Exception{
        Booking testBookingA = TestDataUtil.createTestBookingA();

        String bookingJson = objectMapper.writeValueAsString(testBookingA);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/bookings/notExistingId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void approveBookingShouldUpdateIsApprovedReturnBookingAndOKHttpResponseIfBookingExists() throws Exception {
        Schedule emptyScheduleA = TestDataUtil.createEmptyTestSchedule();
        scheduleService.save(emptyScheduleA);
        Booking testBookingA = TestDataUtil.createTestBookingA();

        String scheduleJson = objectMapper.writeValueAsString(emptyScheduleA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson)
        );

        String bookingJson = objectMapper.writeValueAsString(testBookingA);
        MvcResult mvcResult = mockMvc.perform(
                 MockMvcRequestBuilders.post("/schedules/" + emptyScheduleA.getId() + "/bookings")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(bookingJson)
        ).andReturn();

        testBookingA = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Booking.class);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/bookings/approve/" + testBookingA.getBookingId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.bookingId").value(testBookingA.getBookingId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.approved").value(true)
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void approveBookingShouldReturnNOTFOUNDHttpResponseIfBookingDoesntExists() throws Exception {
        mockMvc.perform(
                MockMvcRequestBuilders.put("/bookings/approve/notExistingId")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void cancelBookingShouldReturnCancelledBookingAndOKHttpResponseIfBookingExists() throws Exception {
        Schedule emptyScheduleA = TestDataUtil.createEmptyTestSchedule();
        scheduleService.save(emptyScheduleA);
        Booking testBookingA = TestDataUtil.createTestBookingA();

        String scheduleJson = objectMapper.writeValueAsString(emptyScheduleA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson)
        );

        String bookingJson = objectMapper.writeValueAsString(testBookingA);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules/" + emptyScheduleA.getId() + "/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson)
        ).andReturn();

        testBookingA = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Booking.class);
        testBookingA.setCancellationNotes("cancellation test note");
        String canceledBookingJson = objectMapper.writeValueAsString(testBookingA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/bookings/cancel/" + testBookingA.getBookingId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(canceledBookingJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.cancellationNotes").value(testBookingA.getCancellationNotes())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.cancellationDate").isNotEmpty()
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void cancelBookingShouldReturnNOTFOUNDHttpResponseIfBookingDoesntExists() throws Exception {
        Schedule emptyScheduleA = TestDataUtil.createEmptyTestSchedule();
        scheduleService.save(emptyScheduleA);
        Booking testBookingA = TestDataUtil.createTestBookingA();

        String scheduleJson = objectMapper.writeValueAsString(emptyScheduleA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson)
        );

        String bookingJson = objectMapper.writeValueAsString(testBookingA);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules/" + emptyScheduleA.getId() + "/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson)
        ).andReturn();

        testBookingA = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Booking.class);
        testBookingA.setCancellationNotes("cancellation test note");
        String canceledBookingJson = objectMapper.writeValueAsString(testBookingA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/bookings/cancel/notExistingId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(canceledBookingJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void cancelBookingShouldReturnNOTACCEPTABLEHttpResponseIfBookingIsAlreadyCanceled() throws Exception {
        Schedule emptyScheduleA = TestDataUtil.createEmptyTestSchedule();
        scheduleService.save(emptyScheduleA);
        Booking testBookingA = TestDataUtil.createTestBookingA();

        String scheduleJson = objectMapper.writeValueAsString(emptyScheduleA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(scheduleJson)
        );

        String bookingJson = objectMapper.writeValueAsString(testBookingA);
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.post("/schedules/" + emptyScheduleA.getId() + "/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson)
        ).andReturn();

        testBookingA = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Booking.class);
        testBookingA.setCancellationNotes("cancellation test note");
        String canceledBookingJson = objectMapper.writeValueAsString(testBookingA);

        mockMvc.perform(
                MockMvcRequestBuilders.put("/bookings/cancel/" + testBookingA.getBookingId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(canceledBookingJson)
        );

        testBookingA.setCancellationNotes("updated test note");
        String updatedBookingJson = objectMapper.writeValueAsString(testBookingA);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/bookings/cancel/" + testBookingA.getBookingId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookingJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotAcceptable()
        );
    }
}