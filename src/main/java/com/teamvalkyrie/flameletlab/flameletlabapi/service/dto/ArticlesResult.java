package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@Data
public class ArticlesResult {
    private List<Article> articles;
}
