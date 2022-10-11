package com.teamvalkyrie.flameletlab.flameletlabapi.repository.helper;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.*;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;
import java.util.List;
import java.util.Set;

public class ChatSearchSpecifications {
    public static Specification<GroupChat> nameOrTagIncludes(String query) {
        return new Specification<GroupChat>() {
            @Override
            public Predicate toPredicate(Root<GroupChat> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join<GroupChat, ChatTag> chatTags = root.join(GroupChat_.tags);
                return criteriaBuilder.or(criteriaBuilder.like(root.get(GroupChat_.name),
                        String.format("%%%s%%", query)), criteriaBuilder.like(chatTags.get(ChatTag_.name),
                        String.format("%%%s%%", query)));
            }
        };
    }

    public static Specification<GroupChat> belongsToOccupationType(Long occupationTypeId) {
        return new Specification<GroupChat>() {
            @Override
            public Predicate toPredicate(Root<GroupChat> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join<GroupChat, OccupationType> groupChat = root.join(GroupChat_.occupationType);
                return criteriaBuilder.equal(groupChat.get(GroupChat_.occupationType.getName()).get(OccupationType_.ID), occupationTypeId);
            }
        };
    }

}
