package com.l7aur.authenticationmicroservice.repository;

import com.l7aur.authenticationmicroservice.model.Authentication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthenticationRepository extends JpaRepository<Authentication, Integer> {
    Optional<Authentication> findAuthenticationById(Integer id);
    Optional<Authentication> findAuthenticationByUsername(String username);
}
