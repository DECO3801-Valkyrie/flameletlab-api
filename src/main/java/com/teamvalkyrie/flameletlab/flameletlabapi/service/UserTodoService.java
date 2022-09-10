package com.teamvalkyrie.flameletlab.flameletlabapi.service;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.Todo;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.User;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.TodoRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;


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

    /**
     * Saves a list of todos to the database, in which
     * each todo is marked as not done
     *
     * @param todoNames the names of the new tasks
     * @return a list of the newly created todo objects
     */
    @Transactional
    public List<Todo> saveNewTodos(List<String> todoNames) {
        List<Todo> todos = new ArrayList<>();

        for (String todoName : todoNames) {
            // saveNewTodo method already saves to
            // the database
            todos.add(saveNewTodo(todoName));
        }

        return todos;
    }

    /**
     * Toggles a todo's done field (done to not done and
     * vice versa)
     * @param id the id of the todo object
     * @return the updated todo object
     */
    @Transactional
    public Todo toggleTodo(long id) {
        Todo currentTodo = todoRepository.getReferenceById(id);
        boolean toggle;
        ZonedDateTime completedTime;


        if (currentTodo.getDone()) {
            toggle = false;
            completedTime = null;
        } else {
            toggle = true;

            // currently just using the system clock
            // in the future logic will need to be introduced
            // so that an individual user's time zone is used
            // instead
            completedTime = ZonedDateTime.now();

        }

        currentTodo.setDateCompleted(completedTime);
        currentTodo.setDone(toggle);
        return todoRepository.save(currentTodo);
    }

    /**
     * Deletes a todo object from the database
     * @param id the id of the todo object
     */
    @Transactional
    public void deleteTodo(long id) {
        todoRepository.deleteById(id);
    }

    public Todo getTodo(long id) {
        return todoRepository.getReferenceById(id);
    }

    /**
     * Gets the list of a user's todos
     * @return list of users todos
     */
    public List<Todo> getTodoList(User user) {
        return new ArrayList<>(todoRepository.findByUser(user));
    }

    public int getNumberOfDoneTodos(User user) {
        // get the database to do it, should be faster
        // than using java to perform counts
        return (int) todoRepository.countByUserAndDone(user, true);
    }

    public int getNumberOfTodos(User user) {
        return (int) todoRepository.countByUser(user);
    }
}