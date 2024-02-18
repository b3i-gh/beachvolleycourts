package com.b3i.beachvolleycourts.controllers;

import com.b3i.beachvolleycourts.domains.User;
import com.b3i.beachvolleycourts.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/users")
    public ResponseEntity<User> createUser(@RequestBody User user){
        userService.save(user);
        return new ResponseEntity(user, HttpStatus.CREATED);
    }

    @GetMapping(path = "/users")
    public List<User> findAllUsers(){
        return userService.findAll();
    }

    @GetMapping(path = "/users/{id}")
    public ResponseEntity<User> findUserById(@PathVariable("id") String id){
        Optional<User> foundUser = userService.findById(id);
        return foundUser.map(User -> {
            return new ResponseEntity<>(User, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/users/{id}")
    public ResponseEntity<User> fullUpdateUser(@PathVariable("id") String id, @RequestBody User user){
        if(!userService.existsById(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else{
            userService.save(user);
            return new ResponseEntity(user, HttpStatus.OK);
        }
    }

    @PatchMapping(path = "/users/{id}")
    public ResponseEntity<User> partialUpdateUser(@PathVariable("id") String id, @RequestBody User user){
        if(!userService.existsById(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else{
            User updatedUser = userService.partialUpdate(id, user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/users/{id}")
    public ResponseEntity deleteUser(@PathVariable("id") String id){
        userService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
