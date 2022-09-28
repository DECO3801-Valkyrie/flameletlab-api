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
    private final float ONE_STAR = 1F;
    private final float TWO_STAR = 2F;
    private final float THREE_STAR = 3F;
    private final float FOUR_STAR = 4F;
    private final float FIVE_STAR = 5F;
    private final WorkplaceRatingRepository workplaceRatingRepository;
    private final UserService userService;
    private final WorkplaceRepository workplaceRepository;

    private final WorkplaceService workplaceService;

    /**
     * Counts the total number of the stars for provided star.
     * e.g. get the total number of 1 star ratings.
     *
     * @param ratings the list of ratings
     * @param stars the stars number (1,2,3,4,5)
     * @return the total for
     */
    private long getStarRatingCountFor(List<WorkplaceRating> ratings, float stars) {
        return ratings.stream().filter(wr -> wr.getRating().equals(stars)).count();
    }


    /**
     * Update the workplace average rating and number of ratings received.
     *
     * @param workplace the workplace to update
     */
    private void updateWorkplaceAverageRatingAndRatingsCount(Workplace workplace) {
        var ratings = getAllRatingsByPlaceId(workplace.getPlaceId());
        long totalOneRatings = getStarRatingCountFor(ratings,ONE_STAR);
        long totalTwoRatings = getStarRatingCountFor(ratings,TWO_STAR);
        long totalThreeRatings = getStarRatingCountFor(ratings,THREE_STAR);
        long totalFourRatings = getStarRatingCountFor(ratings,FOUR_STAR);
        long totalFiveRatings = getStarRatingCountFor(ratings,FIVE_STAR);

        float averageRating = (ONE_STAR*totalOneRatings+TWO_STAR*totalTwoRatings+
                THREE_STAR*totalThreeRatings+FOUR_STAR*totalFourRatings+
                FIVE_STAR*totalFiveRatings)/ratings.size();
        workplace.setAverageRating(averageRating);
        workplace.setReviewsCount(ratings.size());
        this.workplaceRepository.save(workplace);

    }

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
            rating = workplaceRatingRepository.saveAndFlush(rating);
            updateWorkplaceAverageRatingAndRatingsCount(w);
            return rating;
        }).orElseGet(() -> {
            WorkplaceRating rating = new WorkplaceRating();
            Workplace workplace = workplaceService.createNewWorkPlace(request.getPlaceId(), request.getPlaceName(),
                    request.getPlaceLocation());
            rating.setUser(userService.getCurrentLoggedInUser());
            rating.setRating(request.getRating());
            rating.setReview(request.getReview());
            rating.setWorkplace(workplace);
            rating.setCreated(ZonedDateTime.now());
            rating = workplaceRatingRepository.saveAndFlush(rating);
            updateWorkplaceAverageRatingAndRatingsCount(workplace);
            return rating;
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
