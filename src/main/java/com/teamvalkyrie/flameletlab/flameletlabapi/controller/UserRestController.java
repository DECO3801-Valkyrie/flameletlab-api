package com.teamvalkyrie.flameletlab.flameletlabapi.controller;

import com.teamvalkyrie.flameletlab.flameletlabapi.service.UserService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserRegisterRequest;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserRegisterResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;
    private final UserMapper userMapper;
    /**
     *
     * @param userRegistration
     * @return
     */
    @PostMapping("/register")
    ResponseEntity<UserRegisterResponse> registerUser(@Valid @RequestBody UserRegisterRequest userRegistration) throws URISyntaxException {
        var savedUser = userService.save(this.userMapper.userRegisterRequestToUser(userRegistration));

        return ResponseEntity
                .created(new URI("/api/user/" + savedUser.getId()))
                .body(userMapper.userToUserRegisterResponse(savedUser));
    }
}
