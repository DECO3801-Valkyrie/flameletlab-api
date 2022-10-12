package com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper;

import com.kwabenaberko.newsapilib.models.Article;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.ArticleResponse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NewsFeedMapper {
    public List<ArticleResponse> newsFeedToArticleResponses(Map<Article, List<String>> articlesTagsPairs) {
        List<ArticleResponse> responses = new ArrayList<>();

        for (Article article : articlesTagsPairs.keySet()) {
            String name = article.getTitle();
            List<String> tags = articlesTagsPairs.get(article);
            String url = article.getUrl();
            String imgUrl = article.getUrlToImage();

            responses.add(new ArticleResponse(name, tags, url, imgUrl));
        }

        return responses;
    }
}
