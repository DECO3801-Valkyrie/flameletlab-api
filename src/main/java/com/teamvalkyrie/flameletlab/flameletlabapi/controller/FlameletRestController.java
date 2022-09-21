package com.teamvalkyrie.flameletlab.flameletlabapi.controller;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.Todo;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.User;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.FlameletService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.UserService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.UserTodoService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserFlameletCheckConcernedResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserFlameletMoodResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserTodoRequestWithId;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper.UserFlameletMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FlameletRestController {
    private final UserService userService;
    private final UserTodoService userTodoService;
    private final FlameletService flameletService;
    private final UserFlameletMapper flameletMapper;

    @GetMapping("/flamelet")
    public ResponseEntity<UserFlameletCheckConcernedResponse> checkIfFlameletConcerned() {
        User current = userService.getCurrentLoggedInUser();
        Boolean isConcerned = flameletService.checkIfConcerned(current);

        var response = flameletMapper.mapIsConcernedToCheckConcernedResponse(isConcerned);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/flamelet")
    public ResponseEntity<UserFlameletMoodResponse> getMoodForTodo(@Valid @RequestBody UserTodoRequestWithId request) {
        Todo todo = userTodoService.getTodo(request.getId());

        System.out.println(request.getId());

        var response = flameletMapper.mapFlameletMoodToFlameletMoodResponse(
                flameletService.moodForTodo(todo));

        return ResponseEntity.ok(response);
    }
}
