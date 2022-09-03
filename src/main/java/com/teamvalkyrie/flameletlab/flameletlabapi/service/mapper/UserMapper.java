package com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.User;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserAccountResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserRegisterRequest;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserRegisterResponse;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {

    public UserRegisterResponse userToUserRegisterResponse(User user) {
        UserRegisterResponse userRegisterResponse = new UserRegisterResponse();
        userRegisterResponse.setId(user.getId());
        userRegisterResponse.setFullName(user.getFullName());
        userRegisterResponse.setEmail(user.getEmail());

        return userRegisterResponse;
    }

    public UserAccountResponse userToUserAccountResponse(User user) {
        UserAccountResponse userAccountResponse = new UserAccountResponse();
        userAccountResponse.setId(user.getId());
        userAccountResponse.setFullName(user.getFullName());
        userAccountResponse.setEmail(user.getEmail());

        return userAccountResponse;
    }

    public User userRegisterRequestToUser(UserRegisterRequest userRegisterRequest) {
        User user = new User();
        user.setEmail(userRegisterRequest.getEmail());
        user.setPassword(userRegisterRequest.getPassword());
        user.setFullName(userRegisterRequest.getFullName());

        return user;
    }
}
