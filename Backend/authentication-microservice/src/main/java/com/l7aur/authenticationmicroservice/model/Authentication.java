package com.l7aur.authenticationmicroservice.model;

import com.l7aur.authenticationmicroservice.model.util.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "authentication")
@AllArgsConstructor
@NoArgsConstructor
public class Authentication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String username;

    private String password;

    private String jwtToken;

    private Role role;
}
