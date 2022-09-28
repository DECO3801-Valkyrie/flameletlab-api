package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.ZonedDateTime;

@Getter
@Setter
public class UserTodoRequest {
    @NotBlank
    private String name;

    @NotNull
    private Duration estimatedTime;

    @NotNull
    private ZonedDateTime estimatedStart;
}