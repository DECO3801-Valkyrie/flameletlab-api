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
    // we use NewsAPI to get the articles
    private final String apiKey = "73e9473e99ab4939a707f7671a8c4cd6";
    private final String searchEndpoint = "https://newsapi.org/v2/everything";
    private final RestTemplate restTemplate;
    private final List<String> tags = initTags();
    private final CachedArticleRepository cachedArticleRepository;
    private final ArticleTagRepository articleTagRepository;

    /**
     * Gets the number of cached articles stored in the database
     * @return num of cached articles
     */
    private Long getNumCachedArticles() {
        return cachedArticleRepository.count();
    }

    /**
     * Returns the cached articles stored in the database
     * @return the cached articles
     */
    public Set<Article> getCachedArticles() {
        List<CachedArticle> cachedArticles = cachedArticleRepository.findAll();
        Set<Article> articles = new HashSet<>();

        for (CachedArticle cachedArticle : cachedArticles) {
            Article article = new Article();
            article.setUrl(cachedArticle.getUrl());
            article.setUrlToImage(cachedArticle.getUrlToImage());
            article.setTitle(cachedArticle.getTitle());

            // Get each article's tags as well
            for (ArticleTag tag : cachedArticle.getTags()) {
                article.getTags().add(tag.getTagName());
            }
            
               articles.add(article);
        }

        return articles;
    }

    /**
     * Save an individual article to the database
     * @param article
     * @return the article that was saved
     */
    @Transactional
    public CachedArticle saveArticle(Article article) {
        CachedArticle cachedArticle = new CachedArticle();
        cachedArticle.setTitle(article.getTitle());
        cachedArticle.setUrl(article.getUrl());
        cachedArticle.setUrlToImage(article.getUrlToImage());
        cachedArticle.setTags(new HashSet<>());

        // Save the article's tags as well
        for (String tag : article.getTags()) {
            ArticleTag articleTag = new ArticleTag();
            articleTag.setTagName(tag);

            articleTagRepository.save(articleTag);
            cachedArticle.getTags().add(articleTag);
        }

        articleTagRepository.flush();
        return cachedArticleRepository.saveAndFlush(cachedArticle);
    }

    /**
     * Saves a set of articles to the database
     * @param articles
     * @return the saved articles
     */
    @Transactional
    public List<CachedArticle> saveArticles(Set<Article> articles) {
        List<CachedArticle> cachedArticles = new ArrayList<>();

        for (Article article : articles) {
            cachedArticles.add(saveArticle(article));
        }

        return cachedArticles;
    }

    /**
     * Helper function used to initialise the list of possible article tags
     * @return list of article tags
     */
    private List<String> initTags() {
        String[] tags = {"Motivation", "Health", "Insight", "Co-workers", "Tips", "Burnout", "Productivity", "Self-care"};

        return new ArrayList<>(Arrays.asList(tags));
    }

    /**
     * Given a search topic and tags, finds articles on the web related to them.
     * @param searchTerm search topic
     * @param tags additional keywords to further filter the search
     * @return list of found articles
     * @throws RestClientException is something goes wrong during the web search
     */
    private List<Article> performSearch(String searchTerm, List<String> tags) throws RestClientException {
        List<Article> articles = null;

        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(searchEndpoint)
                .queryParam("apiKey", apiKey)
                .queryParam("q", searchTerm);

        ArticlesResult result = restTemplate.getForObject(builder.build().toUri(), ArticlesResult.class);

        // Filter the article list, we don't want fields being null
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
            throw new RestClientException("Articles could not be found!");
        }

        return articles;
    }

    /**
     * Given a user, finds articles that are relevant to them
     * @param user
     * @return relevant articles
     */
    @Transactional
    public Set<Article> getArticles(User user) {
        Set<Article> articles = new HashSet<>();
        Map<String, String> tagPairs = new HashMap<>();

        // The occupation type will be the main search term
        OccupationType occType = user.getOccupationType();

        int numTags = tags.size();
        int[] tagPair = {-1, -1};
        int cacheThreshold = 100;

        // Randomise Tags
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                tagPair[j] = (int) (Math.random() * (numTags - 1));
            }

            tagPairs.put(tags.get(tagPair[0]), tags.get(tagPair[1]));
        }

        try {
            // search on occupation + only one tag
            for (String tag : tags) {
                String searchTerm = occType.getName() + " " + tag;
                List<String> tags = Collections.singletonList(tag);
                articles.addAll(performSearch(searchTerm, tags));
            }

            // search on occupation + 2 tags
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

        // cache articles
        if (getNumCachedArticles() < cacheThreshold) {
            saveArticles(articles);
        }

        return articles;
    }
}
