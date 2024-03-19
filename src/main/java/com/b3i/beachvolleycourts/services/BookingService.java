package com.b3i.beachvolleycourts.services;

import com.b3i.beachvolleycourts.domains.Booking;
import com.b3i.beachvolleycourts.domains.Schedule;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Optional<Booking> findBookingById(String id);

    List<Booking> findBookingsByScheduleId(String scheduleId);

    Booking createBooking(String scheduleId, Booking booking);

    Booking updateBooking(String bookingId, Booking booking);

    Booking approveBooking(String bookingId);

    Booking cancelBooking(String bookingId, String cancellationNote);

}
