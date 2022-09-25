package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkplaceResponse {
    private Long id;
    private String placeId;
    private String name;
    private String location;
    private Float  averageRating;
    private Integer   reviewsCount;
}
