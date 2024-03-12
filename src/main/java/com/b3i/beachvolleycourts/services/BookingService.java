package com.b3i.beachvolleycourts.services;

import com.b3i.beachvolleycourts.domains.Booking;
import com.b3i.beachvolleycourts.domains.Schedule;

import java.util.List;
import java.util.Optional;

public interface BookingService {
    List<Booking> findAll();

    Optional<Booking> findById(String id);
//
//    List<Booking> findByScheduleId(String id);
//
//    boolean existsById(String id);
//
//    Booking save(String scheduleId, Booking booking);
//
//    Booking partialUpdate(String scheduleId, String id, Booking booking);
//
//    void delete(String id);
}
