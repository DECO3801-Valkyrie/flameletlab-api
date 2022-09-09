package com.teamvalkyrie.flameletlab.flameletlabapi.repository;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.Todo;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Long> {
    long countByUserAndDone(User user, Boolean isDone);

    long countByUser(User user);

    List<Todo> findByUser(User user);
}
