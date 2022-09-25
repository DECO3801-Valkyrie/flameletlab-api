package com.teamvalkyrie.flameletlab.flameletlabapi.service;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.Workplace;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.WorkplaceRating;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.WorkplaceRatingRepository;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.WorkplaceRepository;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.WorkplaceRatingRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;


/**
 * A service that encapsulates the business logic regarding workplace ratings
 */
@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class WorkplaceRatingService {

    private final WorkplaceRatingRepository workplaceRatingRepository;
    private final UserService userService;
    private final WorkplaceRepository workplaceRepository;

    private final WorkplaceService workplaceService;


    /**
     * A method that saves a workplace rating.
     * If the workplace is new the workplace is first persisted and then added to the new rating
     * otherwise if the workplace already exist the rating is added under it.
     *
     * @param request the payload containing the request
     * @return a persisted workplaceRating
     */
    @Transactional
    public WorkplaceRating save(WorkplaceRatingRequest request) {
        var workPlaceOptional = workplaceRepository.findByPlaceId(request.getPlaceId());
        return workPlaceOptional.map(w -> {
            WorkplaceRating rating = new WorkplaceRating();
            rating.setUser(userService.getCurrentLoggedInUser());
            rating.setRating(request.getRating());
            rating.setReview(request.getReview());
            rating.setCreated(ZonedDateTime.now());
            rating.setWorkplace(w);
            return workplaceRatingRepository.save(rating);
        }).orElseGet(() -> {
            WorkplaceRating rating = new WorkplaceRating();
            Workplace workplace = workplaceService.createNewWorkPlace(request.getPlaceId(), request.getPlaceName(),
                    request.getPlaceLocation());
            rating.setUser(userService.getCurrentLoggedInUser());
            rating.setRating(request.getRating());
            rating.setReview(request.getReview());
            rating.setWorkplace(workplace);
            rating.setCreated(ZonedDateTime.now());
            return workplaceRatingRepository.save(rating);
        });
    }



    /**
     * Uses the Google Place Id to locate the workplace ratings
     *
     * @param placeId the workplace whose ratings are to be returned
     * @return a list of workplace ratings
     */
    public List<WorkplaceRating> getAllRatingsByPlaceId(String placeId) {
        return workplaceRatingRepository.findAllByPlaceId(placeId);
    }
}
