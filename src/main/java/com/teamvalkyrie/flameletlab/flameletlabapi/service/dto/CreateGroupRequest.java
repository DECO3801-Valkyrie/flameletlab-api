package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateGroupRequest {
    private String name;
    private Long occupationTypeId;
    private List<String> tags;
}
