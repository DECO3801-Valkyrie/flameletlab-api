package com.teamvalkyrie.flameletlab.flameletlabapi.repository;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.Todo;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.ZonedDateTime;
import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    long countByUserAndDone(User user, Boolean isDone);

    long countByUser(User user);

    // [start, end)
    @Query(nativeQuery = true,
            value = "SELECT COUNT(*) FROM todo a WHERE a.user_id = :uid and a.date_completed >= :start and a.date_completed < :end")
    long countByUserAndDateCompletedInRange(@Param("uid") Long uid, @Param("start") ZonedDateTime start,
                                            @Param("end") ZonedDateTime end);

    List<Todo> findByUser(User user);
}
