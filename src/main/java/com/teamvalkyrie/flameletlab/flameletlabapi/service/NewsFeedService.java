package com.teamvalkyrie.flameletlab.flameletlabapi.service;

import com.kwabenaberko.newsapilib.NewsApiClient;
import com.kwabenaberko.newsapilib.models.Article;
import com.kwabenaberko.newsapilib.models.request.EverythingRequest;
import com.kwabenaberko.newsapilib.models.response.ArticleResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.OccupationType;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.User;

import java.util.*;
import java.lang.Math;
import java.util.function.Consumer;

public class NewsFeedService {
    private final String apiKey = "e7cbd37331c143efb71160c7cf2437cb";

    private final NewsApiClient newsFeed = new NewsApiClient(apiKey);
    private final List<String> tags = initTags();

    private List<String> initTags() {
        String[] tags = {"Motivation", "Health", "Insight", "Co-workers", "Tips", "Burnout", "Productivity", "Self-care"};

        return new ArrayList<>(Arrays.asList(tags));
    }

    private void performSearch(String searchTerm, List<String> tags, Map<Article, List<String>> articleTagsPairs) {
        NewsApiClient.ArticlesResponseCallback callback = new NewsApiClient.ArticlesResponseCallback() {
            @Override
            public void onSuccess(ArticleResponse articleResponse) {
                Consumer<Article> consumer = x -> { articleTagsPairs.put(x, tags); };
                articleResponse.getArticles().forEach(consumer);
            }

            @Override
            public void onFailure(Throwable throwable) {
                return;
            }
        };

        EverythingRequest request = new EverythingRequest.Builder().q(searchTerm).build();
        newsFeed.getEverything(request, callback);
    }

    public Map<Article, List<String>> getArticles(User user) {
        Set<Article> articles = new HashSet<>();
        Map<Article, List<String>> articlesTagsPairs = new HashMap<>();
        Map<String, String> tagPairs = new HashMap<>();
        OccupationType occType = user.getOccupationType();

        NewsApiClient.ArticlesResponseCallback callback = new NewsApiClient.ArticlesResponseCallback() {
            @Override
            public void onSuccess(ArticleResponse articleResponse) {
                articles.addAll(articleResponse.getArticles());
            }

            @Override
            public void onFailure(Throwable throwable) {
                return;
            }
        };


        int numTags = tags.size();
        int[] tagPair = {-1, -1};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                tagPair[j] = (int) (Math.random() * (numTags - 1));
            }

            tagPairs.put(tags.get(tagPair[0]), tags.get(tagPair[1]));
        }

        for (String tag : tags) {
            String searchTerm = occType.getName() + " " + tag;
            List<String> tags = Collections.singletonList(tag);
            performSearch(searchTerm, tags, articlesTagsPairs);
        }

        for (Map.Entry<String, String> pair : tagPairs.entrySet()) {
            List<String> tags = new ArrayList<>();
            tags.add(pair.getKey());
            tags.add(pair.getValue());

            String searchTerm = occType.getName() + " " + tags.get(0) + " " + tags.get(1);

            performSearch(searchTerm, tags, articlesTagsPairs);
        }

        return articlesTagsPairs;
    }
}
