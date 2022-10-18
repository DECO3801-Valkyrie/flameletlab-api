package com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper;

import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserFlameletCheckConcernedResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserFlameletMoodResponse;
import org.springframework.stereotype.Service;

@Service
public class UserFlameletMapper {
    /**
     * Builds a flamelet mood response object given a mood
     * @param mood
     * @return mood response object
     */
    public UserFlameletMoodResponse mapFlameletMoodToFlameletMoodResponse(String mood) {
        UserFlameletMoodResponse response = new UserFlameletMoodResponse();

        response.setMood(mood);
        return response;
    }

    /**
     * Builds an object that contains information on whether flamelet is concerned.
     * @param isConcerned
     * @return flamelet is concerned object
     */
    public UserFlameletCheckConcernedResponse mapIsConcernedToCheckConcernedResponse(Boolean isConcerned) {
        var response = new UserFlameletCheckConcernedResponse();

        response.setConcerned(isConcerned);
        return response;
    }
}
