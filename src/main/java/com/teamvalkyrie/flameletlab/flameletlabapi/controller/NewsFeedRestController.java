package com.teamvalkyrie.flameletlab.flameletlabapi.controller;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.User;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.NewsFeedService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.UserService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.Article;
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
    private final NewsFeedService newsFeedService;
    private final UserService userService;

    @GetMapping("/newsfeed")
    public ResponseEntity<List<Article>> getNewsFeed() {
        User curr = userService.getCurrentLoggedInUser();
        List<Article> articles = newsFeedService.getArticles(curr);

        return ResponseEntity.ok(articles);
    }
}
