package com.teamvalkyrie.flameletlab.flameletlabapi.repository.helper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.parameters.P;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ChatSearchCriteria {
    private final Long occupationTypeId;
    private final String query;

    private final Long userId;

    private final Boolean joined;

    public ChatSearchCriteria(Map<String, String> searchOptions) {
        this.query = searchOptions.get("query");
        this.occupationTypeId = searchOptions.containsKey("occupationTypeId") ?
                Long.parseLong(searchOptions.get("occupationTypeId")) : null;
        this.userId = searchOptions.containsKey("userId") ? Long.parseLong(searchOptions.get("userId")) : null;
        this.joined = searchOptions.containsKey("joined");
    }


}
