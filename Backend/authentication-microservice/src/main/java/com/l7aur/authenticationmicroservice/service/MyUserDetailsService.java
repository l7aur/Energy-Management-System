package com.l7aur.authenticationmicroservice.service;

import com.l7aur.authenticationmicroservice.model.Authentication;
import com.l7aur.authenticationmicroservice.repository.AuthenticationRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final AuthenticationRepository authenticationRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Authentication auth = authenticationRepository.findAuthenticationByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new User(
                auth.getUsername(),
                auth.getPassword(),
                List.of(new SimpleGrantedAuthority(auth.getRole().toString()))
        );
    }
}
