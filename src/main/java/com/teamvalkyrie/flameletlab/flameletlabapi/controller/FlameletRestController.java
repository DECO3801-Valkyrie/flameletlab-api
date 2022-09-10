package com.teamvalkyrie.flameletlab.flameletlabapi.controller;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.User;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.FlameletService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.UserService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserFlameletMoodResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper.UserFlameletMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FlameletRestController {
    private final UserService userService;
    private final FlameletService flameletService;
    private final UserFlameletMapper flameletMapper;

    @GetMapping("/flamelet")
    public ResponseEntity<UserFlameletMoodResponse> getUserFlameletMood() {
        User current = userService.getCurrentLoggedInUser();
        String mood = flameletService.getMood(current);

        var response = flameletMapper.mapFlameletMoodToFlameletMoodResponse(mood);

        return ResponseEntity.ok(response);
    }
}
