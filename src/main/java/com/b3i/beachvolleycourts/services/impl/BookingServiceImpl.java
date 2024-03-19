package com.b3i.beachvolleycourts.services.impl;

import com.b3i.beachvolleycourts.domains.Booking;
import com.b3i.beachvolleycourts.domains.Schedule;
import com.b3i.beachvolleycourts.repositories.BookingRepository;
import com.b3i.beachvolleycourts.services.BookingService;
import com.b3i.beachvolleycourts.services.ScheduleService;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
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

    @Override
    public Optional<Booking> findBookingById(String id){
        List<Schedule> schedules = scheduleService.findAll();
        Optional<Booking> booking = schedules.stream()
                .flatMap(schedule -> schedule.getBookings().stream())
                .filter(b -> b.getBookingId().equals(id))
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
            if(schedule.get().getBookings() != null)
                return StreamSupport.stream(
                    schedule.get().getBookings().spliterator(), false)
                    .collect(Collectors.toList());
            else return new ArrayList<>();
        }
    }

    @Override
    public Booking createBooking(String scheduleId, Booking booking) {
        Schedule existingSchedule = scheduleService.findById(scheduleId).orElse(null);
        if(existingSchedule == null)
            throw new RuntimeException("Schedule does not exists");

        booking.setBookingId(new ObjectId().toString());
        booking.setApproved(false);
        booking.setScheduleId(scheduleId);

        if(existingSchedule.getBookings() == null)
            existingSchedule.setBookings(Arrays.asList(booking));
        else
            existingSchedule.getBookings().add(booking);

        scheduleService.save(existingSchedule);

        return booking;
    }

    @Override
    public Booking updateBooking(String bookingId, Booking booking){
        return scheduleService.findById(booking.getScheduleId()).map(existingSchedule -> {
            Booking updatedBooking = new Booking();
            List<Booking> updatedBookings = existingSchedule.getBookings();
            updatedBookings
                    .stream()
                    .filter(b -> b.getBookingId().equals(bookingId))
                    .map(existingBooking -> {
                        Optional.ofNullable((booking.getStartTime())).ifPresent(existingBooking::setStartTime);
                        Optional.ofNullable((booking.getEndTime())).ifPresent(existingBooking::setEndTime);
                        Optional.ofNullable((booking.getName())).ifPresent(existingBooking::setName);
                        Optional.ofNullable((booking.getUserId())).ifPresent(existingBooking::setUserId);
                        Optional.ofNullable((booking.getPlayerList())).ifPresent(existingBooking::setPlayerList);
                        Optional.ofNullable((booking.isApproved())).ifPresent(existingBooking::setApproved);
                        Optional.ofNullable((booking.getNotes())).ifPresent(existingBooking::setNotes);
                        Optional.ofNullable((booking.getCancellationNotes())).ifPresent(existingBooking::setCancellationNotes);
                        Optional.ofNullable((booking.getCancellationDate())).ifPresent(existingBooking::setCancellationDate);
                        Optional.ofNullable((booking.getScheduleId())).ifPresent(existingBooking::setScheduleId);
                        return existingBooking;
                    }).collect(Collectors.toList());
            existingSchedule.setBookings(updatedBookings);
            scheduleService.partialUpdate(existingSchedule.getId(), existingSchedule);
            return booking;
        }).orElseThrow(() -> new RuntimeException("Schedule does not exists"));
    }

    @Override
    public Booking approveBooking(String bookingId) {
        Optional<Booking> booking = findBookingById(bookingId);
        if (booking == null)
            throw new RuntimeException("Booking does not exists");
        else {
            booking.get().setApproved(true);
            updateBooking(booking.get().getBookingId(), booking.get());
            return booking.get();
        }
    }

    @Override
    public Booking cancelBooking(String bookingId, String cancellationNotes){
        Optional<Booking> booking = findBookingById(bookingId);
        if (booking == null)
            throw new RuntimeException("Booking does not exists");
        else {
            booking.get().setCancellationDate(LocalDate.now());
            booking.get().setCancellationNotes(cancellationNotes);
            updateBooking(booking.get().getBookingId(), booking.get());
            return booking.get();
        }
    }
}
