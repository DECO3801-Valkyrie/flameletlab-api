package com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper;

import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserFlameletMoodResponse;
import org.springframework.stereotype.Service;

@Service
public class UserFlameletMapper {
    public UserFlameletMoodResponse mapFlameletMoodToFlameletMoodResponse(String mood) {
        UserFlameletMoodResponse response = new UserFlameletMoodResponse();

        response.setMood(mood);
        return response;
    }
}
