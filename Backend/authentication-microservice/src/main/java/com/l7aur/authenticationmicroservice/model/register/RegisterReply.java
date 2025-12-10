package com.l7aur.authenticationmicroservice.model.register;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegisterReply {
    private String errorMessage;
    private String token;
}
