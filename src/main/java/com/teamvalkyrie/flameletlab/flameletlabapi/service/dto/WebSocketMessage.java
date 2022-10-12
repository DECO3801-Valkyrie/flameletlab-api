package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketMessage {
    private Long userId;
    private String message;
}
