package com.l7aur.authenticationmicroservice.model.login;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginReply {
    private String username;
    private String jwtToken;
}
