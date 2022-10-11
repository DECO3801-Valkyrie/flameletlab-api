package com.teamvalkyrie.flameletlab.flameletlabapi.repository.helper;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.GroupChat;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import static com.teamvalkyrie.flameletlab.flameletlabapi.repository.helper.ChatSearchSpecifications.belongsToOccupationType;
import static com.teamvalkyrie.flameletlab.flameletlabapi.repository.helper.ChatSearchSpecifications.nameOrTagIncludes;
import static org.springframework.data.jpa.domain.Specification.where;

@NoArgsConstructor
public class ChatSearchSpecificationsBuilder {
    private ChatSearchCriteria searchCriteria;

    public ChatSearchSpecificationsBuilder(ChatSearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    public Specification<GroupChat> build() {
        Specification<GroupChat> specifications = where(nameOrTagIncludes(searchCriteria.getQuery()));


        if(searchCriteria.getOccupationTypeId() != null) {
            specifications = specifications.and(belongsToOccupationType(searchCriteria.getOccupationTypeId()));
        }


        return specifications;
    }
}
