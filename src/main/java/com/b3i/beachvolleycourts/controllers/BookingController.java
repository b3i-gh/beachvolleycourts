package com.b3i.beachvolleycourts.controllers;

import com.b3i.beachvolleycourts.domains.Booking;
import com.b3i.beachvolleycourts.domains.Schedule;
import com.b3i.beachvolleycourts.services.BookingService;
import com.b3i.beachvolleycourts.services.ScheduleService;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
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
    public ResponseEntity<Booking> createBooking(@PathVariable("scheduleId") String scheduleId, @RequestBody Booking booking) {
        // Check the existence of the schedule
        Optional<Schedule> foundSchedule = scheduleService.findById(scheduleId);
        if (!foundSchedule.isPresent())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            if (LocalTime.parse(booking.getEndTime()).isAfter(LocalTime.parse(foundSchedule.get().getEndTime()))
                    || LocalTime.parse(booking.getStartTime()).isBefore(LocalTime.parse(foundSchedule.get().getStartTime())))
                return new ResponseEntity(booking, HttpStatus.NOT_ACCEPTABLE);
        }

        // TODO: check maximum schedule capacity in a slot;
        bookingService.createBooking(scheduleId, booking);
        return new ResponseEntity(booking, HttpStatus.CREATED);
    }

    @PutMapping(path = "/bookings/{bookingId}")
    public ResponseEntity<Booking> updateBooking(@PathVariable("bookingId") String bookingId, @RequestBody Booking booking) {
        if(bookingService.findBookingById(bookingId) == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        bookingService.updateBooking(bookingId, booking);
        return new ResponseEntity(booking, HttpStatus.OK);
    }

    @PutMapping(path = "/bookings/approve/{bookingId}")
    public ResponseEntity<Booking> approveBooking(@PathVariable("bookingId") String bookingId) {
        try {
            return new ResponseEntity(bookingService.approveBooking(bookingId), HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping(path = "/bookings/cancel/{bookingId}")
    public ResponseEntity<Booking> cancelBooking(@PathVariable("bookingId") String bookingId, @RequestBody Booking booking){
        Optional <Booking> foundBooking = bookingService.findBookingById(bookingId);
        if(foundBooking == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        else {
            if(foundBooking.get().getCancellationDate() != null)
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);

            String cancellationNotes = booking.getCancellationNotes() == null ? "" : booking.getCancellationNotes();
            return new ResponseEntity(bookingService.cancelBooking(bookingId, cancellationNotes), HttpStatus.OK);
        }
    }
}