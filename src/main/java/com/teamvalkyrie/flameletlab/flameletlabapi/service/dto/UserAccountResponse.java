package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.OccupationType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class UserAccountResponse {
    private Long id;
    private String email;
    private String fullName;
    private OccupationType occupationType;
}
