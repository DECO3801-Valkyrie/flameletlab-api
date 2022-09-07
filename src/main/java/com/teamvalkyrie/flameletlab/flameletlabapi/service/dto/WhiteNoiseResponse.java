package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = {"id"})
public class WhiteNoiseResponse {
    private Long id;
    private String title;
    private Long length;
    private String audioPath;
    private String picture;
    private Long listens;
}
