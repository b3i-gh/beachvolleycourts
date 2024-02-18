package com.b3i.beachvolleycourts.services;

import com.b3i.beachvolleycourts.domains.Schedule;

import java.util.List;
import java.util.Optional;

public interface ScheduleService {
    List<Schedule> findAll();

    Optional<Schedule> findById(String id);

    boolean existsById(String id);

    Schedule save(Schedule schedule);

    Schedule partialUpdate(String id, Schedule schedule);

    void delete(String id);
}
