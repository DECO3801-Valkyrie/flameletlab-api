package com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.WorkplaceRating;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.WorkplaceRatingResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkplaceRatingMapper {

    public WorkplaceRatingResponse workPlaceRatingToWorkplaceRatingResponse(WorkplaceRating wr) {
        WorkplaceRatingResponse workplaceRatingResponse = new WorkplaceRatingResponse();
        workplaceRatingResponse.setCreated(wr.getCreated());
        workplaceRatingResponse.setReview(wr.getReview());
        workplaceRatingResponse.setRating(wr.getRating());
        workplaceRatingResponse.setId(wr.getId());
        return workplaceRatingResponse;
    }

    public List<WorkplaceRatingResponse> workPlaceRatingToWorkplaceRatingResponseList(List<WorkplaceRating> workplaceRatings) {
        return workplaceRatings.stream().map(this::workPlaceRatingToWorkplaceRatingResponse).toList();
    }

}
