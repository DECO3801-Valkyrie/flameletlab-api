package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WorkplaceRatingRequest {
    private String review;
    private Float rating;
    @NotBlank
    private String placeId;
    @NotBlank
    private String placeName;
    @NotBlank
    private String placeLocation;
}
