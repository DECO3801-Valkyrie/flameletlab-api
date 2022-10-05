package com.teamvalkyrie.flameletlab.flameletlabapi.controller;

import com.teamvalkyrie.flameletlab.flameletlabapi.service.UserService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.OccupationTypeResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserAccountResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserRegisterRequest;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserRegisterResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

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
        var user = this.userMapper.userRegisterRequestToUser(userRegistration);
        userService.getOccupationTypeById(userRegistration.getOccupationTypeId()).ifPresent(user::setOccupationType);
        var savedUser = userService.save(user);
        return ResponseEntity
                .created(new URI("/api/user/" + savedUser.getId()))
                .body(userMapper.userToUserRegisterResponse(savedUser));
    }

    @GetMapping("/account")
    ResponseEntity<UserAccountResponse> getAccount() {
        var user = this.userMapper.userToUserAccountResponse(userService.getCurrentLoggedInUser());
        return ResponseEntity.ok().body(user);
    }

    /**
     * {@code GET /occupation-types : get a list of all user occupation types
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and body containing list of occupations
     */
    @GetMapping("/occupation-types")
    ResponseEntity<List<OccupationTypeResponse>> getAllOccupationTypes () {
        return ResponseEntity.ok().body(
                userService.getAllOccupationTypes().stream()
                        .map(ot -> new OccupationTypeResponse(ot.getId(), ot.getName())).toList());
    }

}
