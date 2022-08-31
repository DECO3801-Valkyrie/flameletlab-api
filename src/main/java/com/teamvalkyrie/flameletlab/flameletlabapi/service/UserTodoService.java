package com.teamvalkyrie.flameletlab.flameletlabapi.service;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.Todo;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.User;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.TodoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;


@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserTodoService {

    private final UserService userService;
    private final TodoRepository todoRepository;

    /**
     * Saves a new todo to the database as not done
     * and associated with the current logged in user
     *
     * @param todo the name of the new tasked
     * @return the persisted todo
     */
    @Transactional
    public Todo saveNewTodo(String todo) {
        User currentUser = userService.getCurrentLoggedInUser();

        Todo newTodo = new Todo();
        newTodo.setName(todo);
        newTodo.setUser(currentUser);
        newTodo.setCreated(ZonedDateTime.now()); // @TODO use users timezone
        newTodo.setDateCompleted(null);
        newTodo.setDone(false);
        return todoRepository.save(newTodo);
    }
}
