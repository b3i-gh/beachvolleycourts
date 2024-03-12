package com.b3i.beachvolleycourts.services.impl;

import com.b3i.beachvolleycourts.domains.Booking;
import com.b3i.beachvolleycourts.domains.Schedule;
import com.b3i.beachvolleycourts.repositories.BookingRepository;
import com.b3i.beachvolleycourts.services.BookingService;
import com.b3i.beachvolleycourts.services.ScheduleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookingServiceImpl implements BookingService {
    private BookingRepository bookingRepository;
    private ScheduleService scheduleService;

    public BookingServiceImpl(BookingRepository bookingRepository, ScheduleService scheduleService) {
        this.bookingRepository = bookingRepository;
        this.scheduleService = scheduleService;
    }

    public Optional<Booking> findBookingById(String id){
        List<Schedule> schedules = scheduleService.findAll();
        Optional<Booking> booking = schedules.stream()
                .flatMap(schedule -> schedule.getBookings().stream())
                .filter(b -> b.getId().equals(id))
                .findFirst();
        if(booking.isPresent())
            return booking;
        else
            return null;
    }

    @Override
    public List<Booking> findBookingsByScheduleId(String scheduleId) {
        Optional<Schedule> schedule = scheduleService.findById(scheduleId);
        if (!schedule.isPresent())
            throw new RuntimeException("Schedule does not exists");
        else {
            return StreamSupport.stream(
                    schedule.get().getBookings().spliterator(), false)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public Schedule save(String scheduleId, Booking booking) {
        return scheduleService.findById(scheduleId).map(existingSchedule -> {
            existingSchedule.getBookings().add(booking);
            return scheduleService.partialUpdate(scheduleId, existingSchedule);
        }).orElseThrow(() -> new RuntimeException("Schedule does not exists"));
    }
}
