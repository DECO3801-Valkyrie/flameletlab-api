package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class LoginRequest {
    private String email;
    private String password;
    @Getter(AccessLevel.NONE)
    private Boolean remember;

    public Boolean isRemember() {
        return remember;
    }
}
