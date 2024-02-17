package com.b3i.beachvolleycourts.controllers;

import com.b3i.beachvolleycourts.domains.User;
import com.b3i.beachvolleycourts.services.UserService;
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
import com.b3i.beachvolleycourts.TestDataUtil;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
public class UserControllerTests {

    private UserService userService;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private MongoTemplate mongoTemplate;

    @Autowired
    public UserControllerTests(UserService userService, MockMvc mockMvc, MongoTemplate mongoTemplate) {
        this.userService = userService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        this.mongoTemplate = mongoTemplate;
    }

    @AfterEach
    public void cleanUp(){
        mongoTemplate.remove(new Query(), "users");
    }

    @Test
    public void createUserShouldSaveUserAndReturnCREATEDHttpResponse() throws Exception {
        User testUserA = TestDataUtil.createTestUserB();
        String userJson = objectMapper.writeValueAsString(testUserA);
        mockMvc.perform(
                MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value(testUserA.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value(testUserA.getLastName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(testUserA.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.password").value(testUserA.getPassword())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.medicalCertificateExpiryDate").value(testUserA.getMedicalCertificateExpiryDate().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.membershipExpiryDate").value(testUserA.getMembershipExpiryDate().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.admin").value(testUserA.isAdmin())
        ).andExpect(
                MockMvcResultMatchers.status().isCreated()
        );
    }

    @Test
    public void findAllUsersShouldReturnAllUsers(){

    }

    @Test
    public void findUserByIdShouldReturnTheUserIfTheUserExists(){

    }

    @Test
    public void findUserByIdShouldReturnNOTFOUNDHttpResponseIfTheUserDoesntExist(){

    }

    @Test
    public void fullUpdateUserSHouldUpdateTheUsersAndReturnOKHttpResponse(){

    }

    @Test
    public void fullUpdateUserShouldReturnNOTFOUNDHttpResponseIfTheUserDoesntExist() {
    }

    @Test
    public void partialUpdateUserSHouldUpdateTheUsersAndReturnOKHttpResponse(){

    }

    @Test
    public void partialUpdateUserShouldReturnNOTFOUNDHttpResponseIfTheUserDoesntExist() {
    }

    @Test
    public void deleteShouldDeteleTheUserAndReturnNOCONTENTHttpResponse(){

    }
}