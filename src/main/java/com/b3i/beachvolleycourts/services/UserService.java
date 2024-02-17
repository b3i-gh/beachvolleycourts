package com.b3i.beachvolleycourts.services;

import com.b3i.beachvolleycourts.domains.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> findAll();

    Optional<User> findById(String id);

    boolean existsById(String id);

    User save(User users);

    User partialUpdate(String id, User users);

    void delete(String id);
}
