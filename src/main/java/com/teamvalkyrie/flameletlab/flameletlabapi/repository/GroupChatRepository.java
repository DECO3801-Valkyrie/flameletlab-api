package com.teamvalkyrie.flameletlab.flameletlabapi.repository;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.AnonymousGroupChatUser;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, Long>, JpaSpecificationExecutor<GroupChat> {
    Optional<GroupChat> findChatGroupByName(String name);

    @Query("select gc from GroupChat gc left join fetch gc.anonymousUsers u WHERE gc.id = :id")
    Optional<GroupChat> findOneByIdWithUsers(@Param("id") Long id);

    @Query("select gc from GroupChat gc left join fetch gc.messages gcm WHERE gc.id = :id")
    Optional<GroupChat> findOneByIdWithMessages(@Param("id") Long id);
}
