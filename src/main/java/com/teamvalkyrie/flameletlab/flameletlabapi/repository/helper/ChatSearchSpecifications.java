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
                Subquery<Integer> subQuery = criteriaQuery.subquery(Integer.class);
                Root<AnonymousGroupChatUser> groupsJoined = subQuery.from(AnonymousGroupChatUser.class);
                subQuery
                        .select(criteriaBuilder.literal(1))
                        .where(
                                criteriaBuilder.equal(groupsJoined.join(AnonymousGroupChatUser_.user).get(User_.ID), userId),
                                criteriaBuilder.equal(groupsJoined.join(AnonymousGroupChatUser_.group).get(GroupChat_.ID), root.get(GroupChat_.ID))
                        );
                return criteriaBuilder.exists(subQuery);
            }
        };
    }


    /**
     * SELECT * FROM group_chat AS gc
     * LEFT JOIN anonymous_group_chat_user AS agcu ON agcu.group_chat_id = gc.id
     * LEFT JOIN user AS u ON u.id=agcu.user_id
     * WHERE NOT EXISTS (SELECT * FROM anonymous_group_chat_user AS agcu2 WHERE agcu2.group_chat_id = gc.id AND agcu2.user_id = 3);
     * @param userId
     * @return
     */

    public static Specification<GroupChat> notJoinedBy(Long userId) {
        return new Specification<GroupChat>() {
            @Override
            public Predicate toPredicate(Root<GroupChat> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Subquery<Integer> subQuery = criteriaQuery.subquery(Integer.class);
                Root<AnonymousGroupChatUser> groupsJoined = subQuery.from(AnonymousGroupChatUser.class);
                subQuery
                        .select(criteriaBuilder.literal(1))
                        .where(
                                criteriaBuilder.equal(groupsJoined.join(AnonymousGroupChatUser_.user).get(User_.ID), userId),
                                criteriaBuilder.equal(groupsJoined.join(AnonymousGroupChatUser_.group).get(GroupChat_.ID), root.get(GroupChat_.ID))
                        );
                return criteriaBuilder.exists(subQuery).not();
            }
        };
    }

}
