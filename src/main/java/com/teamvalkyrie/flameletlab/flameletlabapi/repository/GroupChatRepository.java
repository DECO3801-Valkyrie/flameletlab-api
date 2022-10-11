package com.teamvalkyrie.flameletlab.flameletlabapi.repository;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.GroupChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupChatRepository extends JpaRepository<GroupChat, Long> {
    Optional<GroupChat> findChatGroupByName(String name);

    @Query("select gc from GroupChat gc join fetch AnonymousGroupChatUser u WHERE gc.id = :id")
    Optional<GroupChat> findAllByIdWithUsers(@Param("id") Long id);
}
