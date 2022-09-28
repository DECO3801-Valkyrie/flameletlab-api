package com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper;

import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserFlameletCheckConcernedResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserFlameletMoodResponse;
import org.springframework.stereotype.Service;

@Service
public class UserFlameletMapper {
    public UserFlameletMoodResponse mapFlameletMoodToFlameletMoodResponse(String mood) {
        UserFlameletMoodResponse response = new UserFlameletMoodResponse();

        response.setMood(mood);
        return response;
    }

    public UserFlameletCheckConcernedResponse mapIsConcernedToCheckConcernedResponse(Boolean isConcerned) {
        var response = new UserFlameletCheckConcernedResponse();

        response.setConcerned(isConcerned);
        return response;
    }
}
