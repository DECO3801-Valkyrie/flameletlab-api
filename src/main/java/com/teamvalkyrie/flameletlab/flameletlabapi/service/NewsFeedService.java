package com.teamvalkyrie.flameletlab.flameletlabapi.service;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.ArticleTag;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.CachedArticle;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.OccupationType;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.User;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.ArticleTagRepository;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.CachedArticleRepository;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.Article;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.ArticlesResult;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.lang.Math;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class NewsFeedService {
    private final String apiKey = "e7cbd37331c143efb71160c7cf2437cb";
    private final String searchEndpoint = "https://newsapi.org/v2/everything";
    private final RestTemplate restTemplate;
    private final List<String> tags = initTags();
    private final CachedArticleRepository cachedArticleRepository;
    private final ArticleTagRepository articleTagRepository;

    private Long getNumCachedArticles() {
        return cachedArticleRepository.count();
    }

    public List<Article> getCachedArticles() {
        List<CachedArticle> cachedArticles = cachedArticleRepository.findAll();
        List<Article> articles = new ArrayList<>();

        for (CachedArticle cachedArticle : cachedArticles) {
            Article article = new Article();
            article.setUrl(cachedArticle.getUrl());
            article.setUrlToImage(cachedArticle.getUrlToImage());
            article.setTitle(cachedArticle.getTitle());

            for (ArticleTag tag : cachedArticle.getTags()) {
                article.getTags().add(tag.getTagName());
            }

	    articles.add(article);
        }

        return articles;
    }

    @Transactional
    public CachedArticle saveArticle(Article article) {
        CachedArticle cachedArticle = new CachedArticle();
        cachedArticle.setTitle(article.getTitle());
        cachedArticle.setUrl(article.getUrl());
        cachedArticle.setUrlToImage(article.getUrlToImage());
        cachedArticle.setTags(new HashSet<>());

        for (String tag : article.getTags()) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setTagName(tag);

            articleTagRepository.save(articleTag);
            cachedArticle.getTags().add(articleTag);
        }

        articleTagRepository.flush();
        System.out.println(cachedArticle.getUrlToImage());
        System.out.flush();
        return cachedArticleRepository.saveAndFlush(cachedArticle);
    }

    @Transactional
    public List<CachedArticle> saveArticles(List<Article> articles) {
        List<CachedArticle> cachedArticles = new ArrayList<>();

        for (Article article : articles) {
            cachedArticles.add(saveArticle(article));
        }

        return cachedArticles;
    }

    private List<String> initTags() {
        String[] tags = {"Motivation", "Health", "Insight", "Co-workers", "Tips", "Burnout", "Productivity", "Self-care"};

        return new ArrayList<>(Arrays.asList(tags));
    }

    private List<Article> performSearch(String searchTerm, List<String> tags) throws RestClientException {
        List<Article> articles = null;

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(searchEndpoint)
                .queryParam("apiKey", apiKey)
                .queryParam("q", searchTerm);

        ArticlesResult result = restTemplate.getForObject(builder.build().toUri(), ArticlesResult.class);

        if (result != null) {
            articles = result.getArticles();
            articles.forEach(x -> x.setTags(tags));
            articles = articles.stream()
                    .filter(
                            x -> x.getUrl() != null
                                    && x.getTitle() != null
                                    && x.getUrlToImage() != null
                                    && x.getTags() != null)
                    .collect(Collectors.toList());
        } else {
            System.out.println("something went wrong when getting articles!");
        }

        return articles;
    }

    @Transactional
    public List<Article> getArticles(User user) {
        List<Article> articles = new ArrayList<>();
        Map<String, String> tagPairs = new HashMap<>();
        OccupationType occType = user.getOccupationType();

        int numTags = tags.size();
        int[] tagPair = {-1, -1};

        // Randomise Tags
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                tagPair[j] = (int) (Math.random() * (numTags - 1));
            }

            tagPairs.put(tags.get(tagPair[0]), tags.get(tagPair[1]));
        }

        try {
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
        } catch (RestClientException e) {
            return getCachedArticles();
        }

        if (getNumCachedArticles() < 100) {
            saveArticles(articles);
        }

        Collections.shuffle(articles);
        return articles;
    }
}
