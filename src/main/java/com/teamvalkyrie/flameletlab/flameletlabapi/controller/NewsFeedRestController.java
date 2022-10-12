package com.teamvalkyrie.flameletlab.flameletlabapi.controller;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.User;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.NewsFeedService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.UserService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.ArticleResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper.NewsFeedMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NewsFeedRestController {
    private NewsFeedService newsFeedService;

    private UserService userService;

    private NewsFeedMapper newsFeedMapper;

    @GetMapping("/newsfeed")
    public ResponseEntity<List<ArticleResponse>> getNewsFeed() {
        User curr = userService.getCurrentLoggedInUser();
        List<ArticleResponse> articles = newsFeedMapper.newsFeedToArticleResponses(newsFeedService.getArticles(curr));

        return ResponseEntity.ok(articles);
    }
}
