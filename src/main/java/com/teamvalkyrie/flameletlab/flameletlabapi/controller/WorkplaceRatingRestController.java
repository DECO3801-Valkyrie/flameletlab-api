package com.teamvalkyrie.flameletlab.flameletlabapi.controller;

import com.teamvalkyrie.flameletlab.flameletlabapi.service.WorkplaceRatingService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.WorkplaceService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.WorkplaceRatingRequest;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.WorkplaceRatingResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.WorkplaceResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper.WorkplaceMapper;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper.WorkplaceRatingMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class WorkplaceRatingRestController {

    private final WorkplaceRatingService workplaceRatingService;

    private final WorkplaceService workplaceService;
    private final WorkplaceRatingMapper mapper;

    private final WorkplaceMapper workplaceMapper;

    /**
     * {@code POST /workplace-ratings} : create a new workplace rating/review for logged user
     *
     * @param request body containing new review
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the persisted review.
     */
    @PostMapping("/workplace-ratings")
    public ResponseEntity<WorkplaceRatingResponse> addUserReview(@Valid @RequestBody WorkplaceRatingRequest request) {
        var saved = workplaceRatingService.save(request);
        return ResponseEntity.created(URI.create("/api/workplace-ratings/" + saved.getId()))
                .body(mapper.workPlaceRatingToWorkplaceRatingResponse(saved));
    }

    /**
     * {@code GET /workplace-ratings/:placeId} : get all ratings for a place
     *
     * @param placeId the workplace whose ratings are to be returned
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and list of ratings for place
     */
    @GetMapping("/workplace-ratings/{placeId}")
    public ResponseEntity<List<WorkplaceRatingResponse>> getAllRatingsForPlaceId(@PathVariable String placeId) {
       return ResponseEntity
               .ok(mapper.workPlaceRatingToWorkplaceRatingResponseList(workplaceRatingService.getAllRatingsByPlaceId(placeId)));
    }

    /**
     * {@code GET /workplace-ratings/workplaces} : get all ratings for a place
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and list of workplaces
     */
    @GetMapping("/workplace-ratings/workplaces")
    public ResponseEntity<List<WorkplaceResponse>> getAllWorkplaces() {
      return  ResponseEntity.ok()
                .body(workplaceMapper.workplaceToWorkplaceResponseList(workplaceService.getAllWorkplaces()));
    }

    /**
     * {@code GET /workplace-ratings/workplaces/{placeId}} : get a workplace by placeId
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the requested workplace
     */
    @GetMapping("/workplace-ratings/workplaces/{placeId}")
    public ResponseEntity<WorkplaceResponse> getWorkplaceByPlaceId(@PathVariable String placeId) {
        return  ResponseEntity.ok()
                .body(workplaceMapper.workplaceToWorkplaceResponse(workplaceService.getWorkplaceByPlaceId(placeId).get()));
    }
}
