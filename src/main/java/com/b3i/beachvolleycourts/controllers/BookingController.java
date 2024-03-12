package com.b3i.beachvolleycourts.controllers;

import com.b3i.beachvolleycourts.domains.Booking;
import com.b3i.beachvolleycourts.domains.Schedule;
import com.b3i.beachvolleycourts.services.BookingService;
import com.b3i.beachvolleycourts.services.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
public class BookingController {
    private BookingService bookingService;
    private ScheduleService scheduleService;

    public BookingController(ScheduleService scheduleService, BookingService bookingService) {
        this.bookingService = bookingService;
        this.scheduleService = scheduleService;
    }

    @GetMapping(path = "/bookings/{id}")
    public ResponseEntity<Booking> findBookingById(@PathVariable("id") String id) {
        Optional<Booking> foundBooking = bookingService.findBookingById(id);
        return (foundBooking != null) ? new ResponseEntity<>(foundBooking.get(), HttpStatus.OK) : (new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping(path = "/schedules/{scheduleId}/bookings")
    public ResponseEntity<List<Booking>> findBookingsByScheduleId(@PathVariable("scheduleId") String scheduleId) {
        Optional<Schedule> foundSchedule = scheduleService.findById(scheduleId);
        if (!foundSchedule.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            List<Booking> foundBookings = bookingService.findBookingsByScheduleId(scheduleId);
            return new ResponseEntity<>(foundBookings, HttpStatus.OK);
        }
    }

    @PostMapping(path = "/schedules/{scheduleId}/bookings")
    public ResponseEntity<Booking> save(@PathVariable("scheduleId") String scheduleId, @RequestBody Booking booking) {
        Optional<Schedule> foundSchedule = scheduleService.findById(scheduleId);
        if (!foundSchedule.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            if (LocalTime.parse(booking.getEndTime()).isAfter(LocalTime.parse(foundSchedule.get().getEndTime()))
                    || LocalTime.parse(booking.getStartTime()).isBefore(LocalTime.parse(foundSchedule.get().getStartTime())))
                return new ResponseEntity(booking, HttpStatus.NOT_ACCEPTABLE);

            // TODO: check maximum schedule capacity in a slot;

            bookingService.save(scheduleId, booking);
            return new ResponseEntity(booking, HttpStatus.CREATED);
        }
    }
}