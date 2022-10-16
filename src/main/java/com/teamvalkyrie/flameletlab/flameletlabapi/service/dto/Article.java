package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(of = "url")
public class Article {
    private String title;
    private String url;
    private String urlToImage;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<String> tags = new ArrayList<>();
}
