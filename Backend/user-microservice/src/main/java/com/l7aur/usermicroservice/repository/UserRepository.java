package com.l7aur.usermicroservice.repository;

import com.l7aur.usermicroservice.model.User;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<@NonNull User, @NonNull Integer> {
    Optional<User> findByUsername(String username);
}
