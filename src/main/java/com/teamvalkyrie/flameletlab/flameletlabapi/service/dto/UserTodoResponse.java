package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.ZonedDateTime;

@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class UserTodoResponse {

    private Long id;

    private String name;

    private Boolean done;

    private ZonedDateTime created;

    private ZonedDateTime dateCompleted;

    private Long durationInMinutes;

    private Duration estimatedTime;

    private ZonedDateTime estimatedStart;
}
