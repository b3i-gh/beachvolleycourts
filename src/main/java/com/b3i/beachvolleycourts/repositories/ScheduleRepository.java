package com.b3i.beachvolleycourts.repositories;

import com.b3i.beachvolleycourts.domains.Schedule;
import com.b3i.beachvolleycourts.domains.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ScheduleRepository extends MongoRepository<Schedule, String> {
}
