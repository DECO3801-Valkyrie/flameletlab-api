package com.teamvalkyrie.flameletlab.flameletlabapi.service;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.Workplace;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.WorkplaceRating;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.WorkplaceRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WorkplaceService {
    private final WorkplaceRepository workplaceRepository;


    /**
     * Creates a new workplace
     *
     * @param placeId the workplace Id from Google Places API
     * @param name the name from Google Places API
     * @param location the location from Google Places API
     * @return the newly persisted workplace
     */
    @Transactional
    public Workplace createNewWorkPlace(String placeId, String name, String location) {
        Workplace workplace = new Workplace();
        workplace.setPlaceId(placeId);
        workplace.setName(name);
        workplace.setLocation(location);
        workplace.setReviewsCount(0);
        workplace.setAverageRating(0.0f);
        return workplaceRepository.save(workplace);
    }


    /**
     * Gets all the workplaces that have ratings.
     *
     * @return a list of workplaces
     */
    public List<Workplace> getAllWorkplaces() {
        return workplaceRepository.findAll();
    }

}
