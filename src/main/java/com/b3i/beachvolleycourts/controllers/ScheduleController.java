package com.b3i.beachvolleycourts.controllers;

import com.b3i.beachvolleycourts.domains.Schedule;
import com.b3i.beachvolleycourts.services.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
public class ScheduleController {
    private ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping(path = "/schedules")
    public ResponseEntity<Schedule> createSchedule(@RequestBody Schedule schedule){
        if(LocalTime.parse(schedule.getEndTime()).isBefore(LocalTime.parse(schedule.getStartTime())))
            return new ResponseEntity(schedule, HttpStatus.NOT_ACCEPTABLE);
        scheduleService.save(schedule);
        return new ResponseEntity(schedule, HttpStatus.CREATED);
    }

    @GetMapping(path = "/schedules")
    public List<Schedule> findAllSchedules(){
        return scheduleService.findAll();
    }

    @GetMapping(path = "/schedules/{id}")
    public ResponseEntity<Schedule> findScheduleById(@PathVariable("id") String id){
        Optional<Schedule> foundSchedule = scheduleService.findById(id);
        return foundSchedule.map(Schedule -> {
            return new ResponseEntity<>(Schedule, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping(path = "/schedules/{id}")
    public ResponseEntity<Schedule> fullUpdateSchedule(@PathVariable("id") String id, @RequestBody Schedule schedule){
        if(!scheduleService.existsById(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else{
            scheduleService.save(schedule);
            return new ResponseEntity(schedule, HttpStatus.OK);
        }
    }

    @PatchMapping(path = "/schedules/{id}")
    public ResponseEntity<Schedule> partialUpdateSchedule(@PathVariable("id") String id, @RequestBody Schedule schedule){
        if(!scheduleService.existsById(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else{
            Schedule updatedSchedule = scheduleService.partialUpdate(id, schedule);
            return new ResponseEntity<>(updatedSchedule, HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/schedules/{id}")
    public ResponseEntity deleteSchedule(@PathVariable("id") String id){
        scheduleService.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
