package com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.Workplace;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.WorkplaceResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkplaceMapper {


    public WorkplaceResponse workplaceToWorkplaceResponse(Workplace workplace) {
        WorkplaceResponse workplaceResponse = new WorkplaceResponse();
        workplaceResponse.setId(workplace.getId());
        workplaceResponse.setName(workplace.getName());
        workplaceResponse.setLocation(workplace.getLocation());
        workplaceResponse.setPlaceId(workplace.getPlaceId());
        workplaceResponse.setReviewsCount(workplace.getReviewsCount());
        workplaceResponse.setAverageRating(workplace.getAverageRating());

        return workplaceResponse;
    }

    public List<WorkplaceResponse> workplaceToWorkplaceResponseList(List<Workplace> workplaceList) {
        return workplaceList.stream().map(this::workplaceToWorkplaceResponse).toList();
    }
}
