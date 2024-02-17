package com.b3i.beachvolleycourts.services.impl;

import com.b3i.beachvolleycourts.domains.User;
import com.b3i.beachvolleycourts.repositories.UserRepository;
import com.b3i.beachvolleycourts.services.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> findAll() {
        return StreamSupport.stream(userRepository
                                .findAll()
                                .spliterator(),
                        false)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id);
    }

    @Override
    public boolean existsById(String id) {
        return userRepository.existsById(id);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public User partialUpdate(String id, User user) {
        user.setId(id);
        return userRepository.findById(id).map(existingUser -> {
            Optional.ofNullable((user.getEmail())).ifPresent(existingUser::setEmail);
            Optional.ofNullable((user.getPassword())).ifPresent(existingUser::setPassword);
            Optional.ofNullable((user.getFirstName())).ifPresent(existingUser::setFirstName);
            Optional.ofNullable((user.getLastName())).ifPresent(existingUser::setLastName);
            Optional.ofNullable((user.getMembershipExpiryDate())).ifPresent(existingUser::setMembershipExpiryDate);
            Optional.ofNullable((user.getMedicalCertificateExpiryDate())).ifPresent(existingUser::setMedicalCertificateExpiryDate);
            Optional.ofNullable((user.isAdmin())).ifPresent(existingUser::setAdmin);
            return userRepository.save(existingUser);
        }).orElseThrow(() -> new RuntimeException("User does not exists"));
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(id);
    }
}
