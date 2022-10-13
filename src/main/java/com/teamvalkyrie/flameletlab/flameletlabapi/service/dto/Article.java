package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Article {
    private String title;
    private String url;
    private String urlToImage;

    @JsonIgnore
    private List<String> tags;
}
