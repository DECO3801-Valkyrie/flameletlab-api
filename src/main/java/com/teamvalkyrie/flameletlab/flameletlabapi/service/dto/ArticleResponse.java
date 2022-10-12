package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import java.util.List;

public class ArticleResponse {
    public String name;

    public List<String> tags;

    public String url;

    public String imgUrl;

    public ArticleResponse(String name, List<String> tags, String url, String imgUrl) {
        this.name = name;
        this.tags = tags;
        this.url = url;
        this.imgUrl = imgUrl;
    }
}