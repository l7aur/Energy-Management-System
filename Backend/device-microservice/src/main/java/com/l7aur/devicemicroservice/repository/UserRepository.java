package com.l7aur.devicemicroservice.repository;

import com.l7aur.devicemicroservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String admin);

    void deleteAllByUsername(String username);
}
