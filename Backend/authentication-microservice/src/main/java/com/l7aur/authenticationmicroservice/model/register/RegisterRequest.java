package com.l7aur.authenticationmicroservice.model.register;

import com.l7aur.authenticationmicroservice.model.util.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterRequest {
    private String username;
    private String password;
    private Role role;
}
