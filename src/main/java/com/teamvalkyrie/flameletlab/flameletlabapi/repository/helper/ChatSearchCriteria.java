package com.teamvalkyrie.flameletlab.flameletlabapi.repository.helper;

import lombok.Getter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ChatSearchCriteria {
    private final Long occupationTypeId;
    private final String query;
    private Set<String> tags = new HashSet<>();

    public ChatSearchCriteria(Map<String, String> searchOptions) {
        this.query = searchOptions.get("query");
        this.occupationTypeId = searchOptions.containsKey("occupationTypeId") ?
                Long.parseLong(searchOptions.get("occupationTypeId")) : null;
        this.tags = searchOptions.containsKey("tags") ?
                Arrays.stream(searchOptions.get("tags").split(",")).
                collect(Collectors.toSet()) : null;
    }


}
