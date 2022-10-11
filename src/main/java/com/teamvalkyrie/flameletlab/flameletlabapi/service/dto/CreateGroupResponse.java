package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;
import java.util.List;

@Getter
@Setter
public class CreateGroupResponse {

    private Long id;
    private String name;
    private List<String> tags;
    private ZonedDateTime created;

}
