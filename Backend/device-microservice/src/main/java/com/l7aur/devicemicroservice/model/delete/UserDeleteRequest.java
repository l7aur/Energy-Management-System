package com.l7aur.devicemicroservice.model.delete;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class UserDeleteRequest {
    private List<String> usernames;
}
