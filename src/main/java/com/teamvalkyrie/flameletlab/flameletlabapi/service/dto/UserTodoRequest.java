package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import java.time.Duration;

@Getter
@Setter
public class UserTodoRequest {
    @NotBlank
    private String name;

    private Duration estimatedTime;
}