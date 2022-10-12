package com.teamvalkyrie.flameletlab.flameletlabapi.repository;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.AnonymousGroupChatUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AnonymousGroupChatUserRepository extends JpaRepository<AnonymousGroupChatUser, Long> {
    Optional<AnonymousGroupChatUser> findOneByUserId(Long userId);

    @Query(
"""
select agc from AnonymousGroupChatUser agc 
left join fetch agc.group g 
left join fetch agc.user u 
WHERE g.id = :groupId AND u.id = :userId
""")
    AnonymousGroupChatUser findAnonymousUserByUserIdAndGroupId(@Param("userId") Long userId, @Param("groupId") Long groupId);
}
