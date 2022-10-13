package com.teamvalkyrie.flameletlab.flameletlabapi.service;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.OccupationType;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.User;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.Article;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.ArticlesResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.lang.Math;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class NewsFeedService {
    private final String apiKey = "e7cbd37331c143efb71160c7cf2437cb";
    private final String searchEndpoint = "https://newsapi.org/v2/everything";
    private final RestTemplate restTemplate;
    private final List<String> tags = initTags();

    private List<String> initTags() {
        String[] tags = {"Motivation", "Health", "Insight", "Co-workers", "Tips", "Burnout", "Productivity", "Self-care"};

        return new ArrayList<>(Arrays.asList(tags));
    }

    private List<Article> performSearch(String searchTerm, List<String> tags) {
        List<Article> articles = null;

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(searchEndpoint)
                .queryParam("apiKey", apiKey)
                .queryParam("q", searchTerm);

        ArticlesResult result = restTemplate.getForObject(builder.build().toUri(), ArticlesResult.class);

        if (result != null) {
            articles = result.getArticles();
            articles.forEach(x -> x.setTags(tags));
        } else {
            System.out.println("fuck me");
        }

        return articles;
    }

    public List<Article> getArticles(User user) {
        List<Article> articles = new ArrayList<>();
        Map<String, String> tagPairs = new HashMap<>();
        OccupationType occType = user.getOccupationType();

        int numTags = tags.size();
        int[] tagPair = {-1, -1};

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                tagPair[j] = (int) (Math.random() * (numTags - 1));
            }

            tagPairs.put(tags.get(tagPair[0]), tags.get(tagPair[1]));
        }

        System.out.println(tagPairs);

        for (String tag : tags) {
            String searchTerm = occType.getName() + " " + tag;
            List<String> tags = Collections.singletonList(tag);
            articles.addAll(performSearch(searchTerm, tags));
        }

        for (Map.Entry<String, String> pair : tagPairs.entrySet()) {
            List<String> tags = new ArrayList<>();
            tags.add(pair.getKey());
            tags.add(pair.getValue());

            String searchTerm = occType.getName() + " " + tags.get(0) + " " + tags.get(1);

            articles.addAll(performSearch(searchTerm, tags));
        }

        System.out.println("bye bye");
        Collections.shuffle(articles);
        return articles;
    }
}
