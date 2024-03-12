package com.b3i.beachvolleycourts.controllers;

import com.b3i.beachvolleycourts.domains.Booking;
import com.b3i.beachvolleycourts.domains.Schedule;
import com.b3i.beachvolleycourts.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
public class BookingController {
    private BookingService bookingService;

    @GetMapping(path = "/bookings")
    public List<Booking> findAllSchedules(){
        return bookingService.findAll();
    }

    @GetMapping(path = "/bookings/{id}")
    public ResponseEntity<Booking> findBookingById(@PathVariable("id") String id){
        Optional<Booking> foundBooking = bookingService.findById(id);
        return foundBooking.map(booking -> {
            return new ResponseEntity<>(booking, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
