package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class UserTodoRequestWithId {
    private long id;
}