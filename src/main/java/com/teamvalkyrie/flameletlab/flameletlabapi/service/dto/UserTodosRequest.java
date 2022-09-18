package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.util.Map;

@Getter
@Setter
public class UserTodosRequest {
    // todoName : lengthOfTodo
    private Map<String, Duration> newTodos;
}
