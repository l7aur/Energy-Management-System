package com.l7aur.authenticationmicroservice.model.delete;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DeleteRequest {
    private List<Integer> ids;
}
