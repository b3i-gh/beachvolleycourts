package com.b3i.beachvolleycourts.services.impl;

import com.b3i.beachvolleycourts.domains.Schedule;
import com.b3i.beachvolleycourts.repositories.ScheduleRepository;
import com.b3i.beachvolleycourts.services.ScheduleService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ScheduleServiceImpl implements ScheduleService {
    private ScheduleRepository scheduleRepository;

    public ScheduleServiceImpl(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public List<Schedule> findAll() {
        return StreamSupport.stream(scheduleRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Schedule> findById(String id) {
        return scheduleRepository.findById(id);
    }

    @Override
    public boolean existsById(String id) {
        return scheduleRepository.existsById(id);
    }

    @Override
    public Schedule save(Schedule schedule) {
        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule partialUpdate(String id, Schedule schedule) {
        schedule.setId(id);
        return scheduleRepository.findById(id).map(existingSchedule -> {
            Optional.ofNullable((schedule.getDate())).ifPresent(existingSchedule::setDate);
            Optional.ofNullable((schedule.getStartTime())).ifPresent(existingSchedule::setStartTime);
            Optional.ofNullable((schedule.getEndTime())).ifPresent(existingSchedule::setEndTime);
            Optional.ofNullable((schedule.getBookings())).ifPresent(existingSchedule::setBookings);

            return scheduleRepository.save(existingSchedule);
        }).orElseThrow(() -> new RuntimeException("Schedule does not exists"));
    }

    @Override
    public void delete(String id) {
        scheduleRepository.deleteById(id);
    }
}

// TODO find by date, time period?
// TODO filter only free slots?
