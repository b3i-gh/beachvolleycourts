package com.b3i.beachvolleycourts.services.impl;

import com.b3i.beachvolleycourts.domains.Booking;
import com.b3i.beachvolleycourts.domains.Schedule;
import com.b3i.beachvolleycourts.domains.User;
import com.b3i.beachvolleycourts.repositories.BookingRepository;
import com.b3i.beachvolleycourts.repositories.ScheduleRepository;
import com.b3i.beachvolleycourts.services.BookingService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
//    private ScheduleRepository scheduleRepository;

    public BookingServiceImpl(BookingRepository bookingRepository, ScheduleRepository scheduleRepository) {
        this.bookingRepository = bookingRepository;
//        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<Booking> findAll() {
        return StreamSupport.stream(bookingRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Booking> findById(String id) {
        return bookingRepository.findById(id);
    }

//    @Override
//    public List<Booking> findByScheduleId(String id) {
//        Optional<Schedule> schedule = scheduleRepository.findById(id);
//        if(schedule.isPresent())
//            return schedule.get().getBookings();
//        else
//            return null;
//    }
//
//    @Override
//    public boolean existsById(String id) {
//        return bookingRepository.existsById(id);
//    }
//
//    @Override
//    public Booking save(String scheduleId, Booking booking) {
//        return bookingRepository.save(booking);
//    }

//    @Override
//    public Booking partialUpdate(String id, Booking booking) {
//        booking.setId(id);
//        return bookingRepository.findById(id).map(existingBooking -> {
//            Optional.ofNullable((booking.getDate())).ifPresent(existingBooking::setDate);
//            Optional.ofNullable((booking.getStartTime())).ifPresent(existingBooking::setStartTime);
//            Optional.ofNullable((booking.getEndTime())).ifPresent(existingBooking::setEndTime);
//            Optional.ofNullable((booking.getBookings())).ifPresent(existingBooking::setBookings);
//
//            private String id;
//            private String name;
//            private String startTime;
//            private String endTime;
//            private String userId;
//            private List<User> playerList;
//            private boolean isApproved;
//            private String notes;
//            private LocalDate cancellationDate;
//            private String cancellationNotes;
//            return bookingRepository.save(existingBooking);
//        }).orElseThrow(() -> new RuntimeException("Booking does not exists"));
//    }
//
//    @Override
//    public void delete(String id) {
//        bookingRepository.deleteById(id);
//    }
}
