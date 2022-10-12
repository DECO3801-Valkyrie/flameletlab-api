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
                criteriaQuery.groupBy(root.get(GroupChat_.id));
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
                criteriaQuery.groupBy(root.get(GroupChat_.id));
                return criteriaBuilder.equal(groupChat.get(OccupationType_.ID), occupationTypeId);
            }
        };
    }
    public static Specification<GroupChat> joinedBy(Long userId) {
        return new Specification<GroupChat>() {
            @Override
            public Predicate toPredicate(Root<GroupChat> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join<GroupChat, AnonymousGroupChatUser> anonymousGroupChatUserJoin = root.join(GroupChat_.anonymousUsers);
                Join<AnonymousGroupChatUser, User> userJoin = anonymousGroupChatUserJoin.join(AnonymousGroupChatUser_.user);
                criteriaQuery.groupBy(root.get(GroupChat_.id));
                return criteriaBuilder.equal(userJoin.get(User_.ID), userId);
            }
        };
    }

    public static Specification<GroupChat> notJoinedBy(Long userId) {
        return new Specification<GroupChat>() {
            @Override
            public Predicate toPredicate(Root<GroupChat> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Join<GroupChat, AnonymousGroupChatUser> anonymousGroupChatUserJoin = root.join(GroupChat_.anonymousUsers);
                Join<AnonymousGroupChatUser, User> userJoin = anonymousGroupChatUserJoin.join(AnonymousGroupChatUser_.user);
                criteriaQuery.groupBy(root.get(GroupChat_.id));
                return criteriaBuilder.notEqual(userJoin.get(User_.ID), userId);
            }
        };
    }

}
