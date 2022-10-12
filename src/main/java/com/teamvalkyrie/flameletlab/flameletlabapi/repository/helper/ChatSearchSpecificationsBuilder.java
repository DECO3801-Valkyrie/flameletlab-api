package com.teamvalkyrie.flameletlab.flameletlabapi.repository.helper;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.GroupChat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.teamvalkyrie.flameletlab.flameletlabapi.repository.helper.ChatSearchSpecifications.*;
import static org.springframework.data.jpa.domain.Specification.where;

@NoArgsConstructor
public class ChatSearchSpecificationsBuilder {
    private ChatSearchCriteria searchCriteria;

    public ChatSearchSpecificationsBuilder(ChatSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public Specification<GroupChat> build() {
        Specification<GroupChat> specifications = null;
        if (searchCriteria.getQuery() != null && !searchCriteria.getQuery().isBlank()) {
            specifications = where(nameOrTagIncludes(searchCriteria.getQuery()));
        }

        if(specifications == null && searchCriteria.getOccupationTypeId() != null) {
            specifications = where(belongsToOccupationType(searchCriteria.getOccupationTypeId()));
        } else if (specifications != null && searchCriteria.getOccupationTypeId() != null) {
            specifications = specifications.and(belongsToOccupationType(searchCriteria.getOccupationTypeId()));
        }

        if (specifications == null && searchCriteria.getJoined()) {
            specifications = where(joinedBy(searchCriteria.getUserId()));
        } else if(specifications != null && searchCriteria.getJoined()) {
            specifications = specifications.and(joinedBy(searchCriteria.getUserId()));
        }

        if (specifications == null && !searchCriteria.getJoined()) {
            specifications = where(notJoinedBy(searchCriteria.getUserId()));
        } else if(specifications != null && !searchCriteria.getJoined()) {
            specifications = specifications.and(notJoinedBy(searchCriteria.getUserId()));
        }

        return specifications;
    }
}
