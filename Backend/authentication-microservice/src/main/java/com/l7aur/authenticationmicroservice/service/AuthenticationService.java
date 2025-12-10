package com.l7aur.authenticationmicroservice.service;

import com.l7aur.authenticationmicroservice.model.Authentication;
import com.l7aur.authenticationmicroservice.model.util.Role;
import com.l7aur.authenticationmicroservice.model.delete.DeleteReply;
import com.l7aur.authenticationmicroservice.model.delete.DeleteRequest;
import com.l7aur.authenticationmicroservice.model.login.LoginReply;
import com.l7aur.authenticationmicroservice.model.login.LoginRequest;
import com.l7aur.authenticationmicroservice.model.register.RegisterReply;
import com.l7aur.authenticationmicroservice.model.register.RegisterRequest;
import com.l7aur.authenticationmicroservice.repository.AuthenticationRepository;
import com.l7aur.authenticationmicroservice.repository.TopicPublisher;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationRepository authenticationRepository;
    private final TopicPublisher topicPublisher;

    public ResponseEntity<@NonNull RegisterReply> register(RegisterRequest request) {
        try {
            if (authenticationRepository.findAuthenticationByUsername(request.getUsername()).isPresent()){
                throw new RuntimeException(request.getUsername() + " already exists!");
            }

            Authentication authentication = new Authentication(
                    null,
                    request.getUsername(),
                    passwordEncoder.encode(request.getPassword()),
                    jwtService.generateToken(request.getUsername(), Role.CLIENT),
                    request.getRole());
            authenticationRepository.save(authentication);
            topicPublisher.requestUserServiceNewUser(authentication.getUsername());
            return new ResponseEntity<>(new RegisterReply(null, authentication.getJwtToken()), HttpStatus.CREATED);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new RegisterReply(e.getMessage(), ""), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<@NonNull LoginReply> login(LoginRequest request) {
        try {
            Authentication auth = authenticationRepository.findAuthenticationByUsername(request.getUsername())
                    .orElseThrow();

            if(!passwordEncoder.matches(request.getPassword(), auth.getPassword()))
                throw new RuntimeException("Passwords don't match");

            auth.setJwtToken(jwtService.generateToken(auth.getUsername(), auth.getRole()));
            Authentication result = authenticationRepository.save(auth);
            return new ResponseEntity<>(new LoginReply(
                    result.getUsername(),
                    result.getJwtToken()), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<>(new LoginReply(), HttpStatus.UNAUTHORIZED);
        }
    }

    public ResponseEntity<@NonNull List<Authentication>> findAll() {
        try {
            return new ResponseEntity<>(authenticationRepository.findAll(), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<@NonNull Authentication> update(Authentication authentication) {
        try {
            var existingAuthOpt = authenticationRepository.findAuthenticationById(authentication.getId());
            if (existingAuthOpt.isEmpty()) {
                return new ResponseEntity<>(authentication, HttpStatus.NOT_FOUND);
            }
            var existingAuth = existingAuthOpt.get();
            String oldUsername = existingAuth.getUsername();
            String newUsername = authentication.getUsername();
            existingAuth.setUsername(newUsername);
            if (!authentication.getPassword().equals(existingAuth.getPassword())) {
                existingAuth.setPassword(passwordEncoder.encode(authentication.getPassword()));
            }
            existingAuth.setRole(authentication.getRole());
            if (!oldUsername.equals(newUsername)) {
                topicPublisher.requestUserServiceUpdateUser(oldUsername, newUsername);
            }
            var saved = authenticationRepository.save(existingAuth);
            return new ResponseEntity<>(saved, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(authentication, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public ResponseEntity<@NonNull DeleteReply> delete(DeleteRequest request) {
        try {
            if(!request.getIds().isEmpty()) {
                authenticationRepository.deleteAllById(request.getIds());
                topicPublisher.requestUserServiceDeleteUsers(request.getIds());
            }
            return new ResponseEntity<>(new DeleteReply(request.getIds(), null), HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(new DeleteReply(request.getIds(), e.toString()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
