package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GroupChatMessageResponse {
    private Long id;
    private String anonymousUserName;
    private String anonymousImage;
    private String message;
    private ZonedDateTime sent;
}
