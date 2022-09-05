package com.teamvalkyrie.flameletlab.flameletlabapi.controller;

import com.teamvalkyrie.flameletlab.flameletlabapi.service.UserTodoService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserTodoRequest;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserTodoResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserTodoRequestWithId;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper.UserTodoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TodoRestController {

    private final UserTodoService userTodoService;
    private final UserTodoMapper userTodoMapper;


    @PostMapping("/todo")
    public ResponseEntity<UserTodoResponse> createTodo(@Valid @RequestBody UserTodoRequest request) throws URISyntaxException {
        var response = userTodoMapper
                .mapTodoToUserTodoResponse(userTodoService.saveNewTodo(request.getName()));

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
}
