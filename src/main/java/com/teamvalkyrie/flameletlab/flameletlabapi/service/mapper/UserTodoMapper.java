package com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.Todo;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserTodoRequest;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserTodoResponse;
import org.springframework.stereotype.Service;

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
}
