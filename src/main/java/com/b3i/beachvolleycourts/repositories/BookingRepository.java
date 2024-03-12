package com.b3i.beachvolleycourts.repositories;

import com.b3i.beachvolleycourts.domains.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingRepository extends MongoRepository<Booking, String> {
}
