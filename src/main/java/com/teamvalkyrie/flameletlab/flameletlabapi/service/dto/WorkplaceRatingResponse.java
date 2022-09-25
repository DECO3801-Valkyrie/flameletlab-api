package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
public class WorkplaceRatingResponse {
    private Long id;
    private String review;
    private Float rating;
    private ZonedDateTime created;

}
