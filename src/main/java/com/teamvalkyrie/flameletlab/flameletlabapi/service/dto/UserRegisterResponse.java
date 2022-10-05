package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.OccupationType;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserRegisterResponse {
    private Long id;
    private String fullName;
    private String email;
    private OccupationType occupationType;
}
