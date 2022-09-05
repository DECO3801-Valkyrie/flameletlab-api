package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class UserTodosResponse {
    private final ArrayList<UserTodoResponse> todos;

    public UserTodosResponse() {
        todos = new ArrayList<>();
    }
}
