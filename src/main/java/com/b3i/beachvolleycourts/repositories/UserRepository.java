package com.b3i.beachvolleycourts.repositories;

import com.b3i.beachvolleycourts.domains.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {

}
