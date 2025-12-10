package com.l7aur.usermicroservice.model.delete;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class DeleteRequest {
    private List<Integer> ids;
}
