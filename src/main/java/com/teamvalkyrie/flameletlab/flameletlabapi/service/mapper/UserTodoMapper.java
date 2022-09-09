package com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.Todo;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserTodoResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserTodosResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserTodoMapper {

    public UserTodoResponse mapTodoToUserTodoResponse(Todo todo) {
        UserTodoResponse userTodoResponse = new UserTodoResponse();
        userTodoResponse.setName(todo.getName());
        userTodoResponse.setId(todo.getId());
        userTodoResponse.setCreated(todo.getCreated());
        userTodoResponse.setDateCompleted(todo.getDateCompleted());
        userTodoResponse.setDone(todo.getDone());
        return userTodoResponse;
    }

    public UserTodosResponse mapTodoListToUserTodosResponse(List<Todo> todos) {
        UserTodosResponse response = new UserTodosResponse();
        ArrayList<UserTodoResponse> todosResponse = response.getTodos();

        for (Todo todo : todos) {
            todosResponse.add(mapTodoToUserTodoResponse(todo));
        }

        return response;
    }
}
