package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class UserTodosRequest {
    // one to one correspondence between
    // each list

    private List<String> names;

    private List<Duration> estimatedDurations;

    private List<ZonedDateTime> estimatedStarts;
}
