package com.teamvalkyrie.flameletlab.flameletlabapi.controller;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.User;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.UserTodoService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.UserService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.*;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper.UserTodoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TodoRestController {

    private final UserTodoService userTodoService;
    private final UserTodoMapper userTodoMapper;
    private final UserService userService;

    @PostMapping("/todo")
    public ResponseEntity<UserTodoResponse> createTodo(@Valid @RequestBody UserTodoRequest request) throws URISyntaxException {
        var response = userTodoMapper
                .mapTodoToUserTodoResponse(userTodoService.saveNewTodo(request.getName(), request.getEstimatedTime(),
                        request.getEstimatedStart()));

       return ResponseEntity.created(new URI("/api/todo/" + response.getId())).body(response);
    }

    @PutMapping("/todo")
    public ResponseEntity<UserTodoResponse> toggleTodo(@Valid @RequestBody UserTodoRequestWithId request) {
        var response = userTodoMapper
                .mapTodoToUserTodoResponse((userTodoService.toggleTodo(request.getId())));

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/todo")
    public ResponseEntity<Void> deleteTodo(@Valid @RequestBody UserTodoRequestWithId request) {
        userTodoService.deleteTodo(request.getId());

        return ResponseEntity.ok().build();
    }

    @GetMapping("/todo")
    public ResponseEntity<UserTodoResponse> getTodo(@Valid @RequestBody UserTodoRequestWithId request) {
        var response = userTodoService.getTodo(request.getId());

        return ResponseEntity.ok(userTodoMapper.mapTodoToUserTodoResponse(response));
    }

    @GetMapping("/todos")
    public ResponseEntity<UserTodosResponse> getTodos() {
        User current = userService.getCurrentLoggedInUser();
        var response = userTodoMapper.mapTodoListToUserTodosResponse(userTodoService.getTodoList(current));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/todos")
    public ResponseEntity<List<ResponseEntity<UserTodoResponse>>> addTodos(@Valid @RequestBody UserTodosRequest request) throws URISyntaxException {
        List<ResponseEntity<UserTodoResponse>> responses = new ArrayList<>();

        if (!userTodoService.validTodosRequest(request)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        var overallResponse = userTodoMapper.mapTodoListToUserTodosResponse(
                userTodoService.saveNewTodos(request.getNames(), request.getEstimatedDurations(), request.getEstimatedStarts()));

        for (UserTodoResponse response : overallResponse.getTodos()) {
            responses.add(ResponseEntity.created(new URI("/api/todo" + response.getId())).body(response));
        }

        return ResponseEntity.ok(responses);
    }

    @DeleteMapping("/todos")
    public ResponseEntity<Void> deleteUserTodos() {
        User current = userService.getCurrentLoggedInUser();
        userTodoService.deleteUserTodos(current);

        return ResponseEntity.ok().build();
    }
}
