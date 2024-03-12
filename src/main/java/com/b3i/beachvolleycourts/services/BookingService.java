package com.b3i.beachvolleycourts.services;

import com.b3i.beachvolleycourts.domains.Booking;
import com.b3i.beachvolleycourts.domains.Schedule;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Optional<Booking> findBookingById(String id);

    List<Booking> findBookingsByScheduleId(String scheduleId);

    Schedule save(String scheduleId, Booking booking);
}
