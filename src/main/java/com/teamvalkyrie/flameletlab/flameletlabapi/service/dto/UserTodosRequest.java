package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class UserTodosRequest {
    // todoName : lengthOfTodo
    // private Map<String, Duration> newTodos;

    // one to one correspondence between
    // each list

    private List<String> names;

    private List<Duration> durations;

    private List<ZonedDateTime> estimatedStarts;
}
