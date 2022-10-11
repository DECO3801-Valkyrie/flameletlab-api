package com.teamvalkyrie.flameletlab.flameletlabapi.service;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.Todo;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.User;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.TodoRepository;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.UserTodosRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
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
     * @param estimatedTime estimated len of how long a task will take
     * @param estimatedStart when the user thinks they will start the task
     * @return the persisted todo
     */
    @Transactional
    public Todo saveNewTodo(String todo, Duration estimatedTime, ZonedDateTime estimatedStart) {
        User currentUser = userService.getCurrentLoggedInUser();
        Todo newTodo = new Todo();

        newTodo.setName(todo);
        newTodo.setUser(currentUser);
        newTodo.setCreated(ZonedDateTime.now()); // @TODO use users timezone
        newTodo.setDateCompleted(null);
        newTodo.setDone(false);
        newTodo.setEstimatedTime(estimatedTime);
        newTodo.setEstimatedStart(estimatedStart);

        return todoRepository.saveAndFlush(newTodo);
    }

    /**
     * Given list fields for todos, saves todos to the database.
     * Each list should have the exact same length as all others.
     * Each list element should directly correspond to each other.
     * I.e the elements in position 0 for each list are used create
     * a todo element.
     *
     * @param names todo names
     * @param durations todo durations
     * @param estimatedStarts todo estimated starting date-times
     * @return a list of the newly created todo objects
     */
    @Transactional
    public List<Todo> saveNewTodos(List<String> names, List<Duration> durations, List<ZonedDateTime> estimatedStarts) {
        List<Todo> todos = new ArrayList<>();

        for (int i = 0; i < names.size(); i++) {
            // saveNewTodo method already saves to
            // the database
            todos.add(saveNewTodo(names.get(i), durations.get(i), estimatedStarts.get(i)));
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
        return todoRepository.saveAndFlush(currentTodo);
    }

    /**
     * Deletes a todo object from the database
     * @param id the id of the todo object
     */
    @Transactional
    public void deleteTodo(long id) {
        todoRepository.deleteById(id);
        todoRepository.flush();
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

    @Transactional
    public void deleteUserTodos(User user) {
        todoRepository.deleteByUser(user);
        todoRepository.flush();
    }

    public int getNumberOfDoneTodos(User user) {
        // get the database to do it, should be faster
        // than using java to perform counts
        return (int) todoRepository.countByUserAndDone(user, true);
    }

    public int getNumberOfTodos(User user) {
        return (int) todoRepository.countByUser(user);
    }

    public long getNumberDoneTodosForDay(User user, LocalDate date, ZoneId timeZone) {
        // TODO : do this for euphoric
        ZonedDateTime startOfDay = ZonedDateTime.of(date, LocalTime.MIDNIGHT, timeZone);
        ZonedDateTime startOfNextDay = startOfDay.plusDays(1);

        return todoRepository.countByUserAndDateCompletedInRange(user.getId(), startOfDay, startOfNextDay);
    }

    public boolean validTodosRequest(UserTodosRequest request) {
        int namesLen = request.getNames().size();
        int durationsLen = request.getEstimatedDurations().size();
        int estimatedStartsLen = request.getEstimatedStarts().size();

        List<Integer> lens = Arrays.asList(namesLen, durationsLen, estimatedStartsLen);

        return lens.stream().allMatch(x -> x.equals(lens.get(0)));
    }
}