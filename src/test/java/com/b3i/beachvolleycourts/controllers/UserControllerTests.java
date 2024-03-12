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
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import com.b3i.beachvolleycourts.TestDataUtil;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;


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
        User testUserA = TestDataUtil.createTestUserA();
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
    public void findAllUsersShouldReturnAllUsers() throws Exception {
        User testUserA = TestDataUtil.createTestUserA();
        User testUserB = TestDataUtil.createTestUserB();
        userService.save(testUserA);
        userService.save(testUserB);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].id").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].id").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[0].firstName").value(testUserA.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$[1].firstName").value(testUserB.getFirstName())
        );
    }

    @Test
    public void findUserByIdShouldReturnTheUserAndOKHttpResponseIfTheUserExists() throws Exception{
        User testUserA = TestDataUtil.createTestUserA();
        userService.save(testUserA);

        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/" + testUserA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").value(testUserA.getId())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value(testUserA.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value(testUserA.getLastName())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void findUserByIdShouldReturnNOTFOUNDHttpResponseIfTheUserDoesntExist() throws Exception{
        mockMvc.perform(
                MockMvcRequestBuilders.get("/users/notExistingId")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void fullUpdateUserShouldUpdateTheUsersAndReturnOKHttpResponse() throws Exception{
        User testUserA = TestDataUtil.createTestUserA();
        userService.save(testUserA);

        User updatedUser = TestDataUtil.createTestUserA();
        updatedUser.setId(testUserA.getId());
        updatedUser.setFirstName("UPDATED first name");
        updatedUser.setFirstName("UPDATED last name");
        updatedUser.setEmail("updated@updated.com");
        updatedUser.setPassword("updatedPassword");
        updatedUser.setAdmin(false);
        updatedUser.setMembershipExpiryDate(LocalDate.now());
        updatedUser.setMembershipExpiryDate(LocalDate.now());

        String userJson = objectMapper.writeValueAsString(updatedUser);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/" + testUserA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value(updatedUser.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value(updatedUser.getLastName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.email").value(updatedUser.getEmail())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.password").value(updatedUser.getPassword())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.medicalCertificateExpiryDate").value(updatedUser.getMedicalCertificateExpiryDate().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.membershipExpiryDate").value(updatedUser.getMembershipExpiryDate().toString())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.admin").value(updatedUser.isAdmin())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void fullUpdateUserShouldReturnNOTFOUNDHttpResponseIfTheUserDoesntExist() throws Exception {
        User testUserA = TestDataUtil.createTestUserA();
        String userJson = objectMapper.writeValueAsString(testUserA);
        mockMvc.perform(
                MockMvcRequestBuilders.put("/users/notExistingId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void partialUpdateUserShouldUpdateTheUsersAndReturnOKHttpResponse() throws Exception{
        User testUserA = TestDataUtil.createTestUserA();
        userService.save(testUserA);

        User updatedUser = TestDataUtil.createTestUserA();
        updatedUser.setFirstName("UPDATED first name");
        String userJson = objectMapper.writeValueAsString(updatedUser);

        mockMvc.perform(
                MockMvcRequestBuilders.patch("/users/" + testUserA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.id").isString()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.firstName").value(updatedUser.getFirstName())
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.lastName").value(testUserA.getLastName())
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        );
    }

    @Test
    public void partialUpdateUserShouldReturnNOTFOUNDHttpResponseIfTheUserDoesntExist() throws Exception{
        User testUserA = TestDataUtil.createTestUserA();
        String userJson = objectMapper.writeValueAsString(testUserA);
        mockMvc.perform(
                MockMvcRequestBuilders.patch("/users/notExistingId")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userJson)
        ).andExpect(
                MockMvcResultMatchers.status().isNotFound()
        );
    }

    @Test
    public void deleteShouldDeleteTheUserAndReturnNOCONTENTHttpResponse() throws Exception{
        User testUserA = TestDataUtil.createTestUserA();
        userService.save(testUserA);
        mockMvc.perform(
                MockMvcRequestBuilders.delete("/users/" + testUserA.getId())
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(
                MockMvcResultMatchers.status().isNoContent()
        );
    }
}