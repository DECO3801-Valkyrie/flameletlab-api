package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupChatResponse {
    private Long id;
    private String name;
    private List<String> tags;
    private Integer totalUsers;
}
