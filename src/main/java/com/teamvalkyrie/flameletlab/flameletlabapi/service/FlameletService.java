package com.teamvalkyrie.flameletlab.flameletlabapi.service;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.Flamelet;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.Todo;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.User;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.FlameletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.*;
import java.util.*;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class FlameletService {
    private enum Mood {
        CONCERNED,
        NEUTRAL,
        HAPPY,
        EXCITED,
        JOYFUL,
        EXHILARATED,
        EUPHORIC
    }

    private static final Duration dailyTreshold = initDailyTreshold();

    private static Duration initDailyTreshold() {
        int hours = 8;

        return Duration.ofHours(hours);
    }

    private static final List<Mood> normalPositiveMoods = initNormalPositiveMoods();

    private static List<Mood> initNormalPositiveMoods() {
        Mood[] positiveMoods = {Mood.HAPPY, Mood.EXCITED, Mood.JOYFUL, Mood.EXHILARATED};

        return new ArrayList<>(Arrays.asList(positiveMoods));
    }

    // number of todo nodes for a threshold to be reached
    // used in logic for calculating flamelet's mood
    private static final int todoThreshold = 4;

    private final UserTodoService userTodoService;

    private final FlameletRepository flameletRepository;

    /**
     * Helper method to validate a string representing a mood
     * for a mood to be valid it must be one of the following
     * {"neutral", "happy", "excited", "joyful", "exhilarated', "euphoric"}
     * (caps doesn't matter)
     * if the mood is valid, the function doesn't throw anything
     * @param mood
     * @throws IllegalArgumentException if the mood is invalid
     */
    private void validateMood(String mood) throws IllegalArgumentException {
        // trys to coerce mood to a Mood enum
        Mood.valueOf(mood.toUpperCase());
    }

    /**
     * Helper method to get a user's flamelet object
     * if it doesn't exist, it will create it first
     * @param user
     * @return the flamelet object
     */
    private Flamelet getUsersFlamelet(User user) {
        Optional<Flamelet> optionalFlamelet = flameletRepository.findByUser(user);

        // if flamelet doesn't exist, make it
        if (optionalFlamelet.isEmpty()) {
            return createFlamelet(user);
        }

        return optionalFlamelet.get();
    }

    private boolean todoOverdue(Todo todo) {
        if (todo.getDateCompleted() != null) {
            return false;
        }

        Duration estimatedTime = todo.getEstimatedTime();
        ZonedDateTime dueByTime = todo.getCreated().plus(estimatedTime);
        ZonedDateTime currTime = ZonedDateTime.now(dueByTime.getZone());

        return currTime.isAfter(dueByTime);
    }

    private boolean anyTodoOverdue(List<Todo> todos) {
        boolean anyOverdue = false;

        for (Todo todo : todos) {
            if (todoOverdue(todo)) {
                anyOverdue = true;
                break;
            }
        }

        return anyOverdue;
    }

    private ZonedDateTime getMidnightNextDay(ZonedDateTime dateTime) {

        return dateTime.plusDays(1).toLocalDate().
                atStartOfDay(dateTime.getZone());

    }

    private Duration timeLeftInDay(ZonedDateTime dateTime) {
        // get the dateTime for the day tommorow, then roll it back to
        // midnight
        ZonedDateTime midnightNextDay = getMidnightNextDay(dateTime);

        return Duration.between(dateTime, midnightNextDay);
    }

    /**
     * Helper method for overThresholdAnyDay. Given sortedTimes map, a start date and
     * duration, maps the date to the duration. If the duration spans over multiple days,
     * multiple dates will be mapped, with the duration split over the dates.

     * The dates mapped may be different to what was specified as the method shifts the timezone
     * to the system default to maintain consistency.
     * @param sortedTimes map date -> list of durations of tasks for the date
     * @param estimatedStart when the task if hopefully started
     * @param estimatedDuration how long the task hopefully takes
     */
    private void addTime(Map<LocalDate, List<Duration>> sortedTimes,
                          ZonedDateTime estimatedStart, Duration estimatedDuration) {
        // make sure all times are on the same zone
        ZoneId commonTimeZone = ZoneId.systemDefault();
        estimatedStart = estimatedStart.withZoneSameInstant(commonTimeZone);


        ZonedDateTime estimatedFinish = estimatedStart.plus(estimatedDuration);
        LocalDate estFinishDate = estimatedFinish.toLocalDate();
        LocalDate estStartDate = estimatedStart.toLocalDate();
        List<Duration> durations;

        if (estFinishDate == estStartDate) {
            durations = sortedTimes.computeIfAbsent(estFinishDate, k -> new ArrayList<>());
            durations.add(estimatedDuration);
            //sortedTimes.get(estFinishDate).add(estimatedDuration);
            return;
        }

        // handles the case where a user adds a task during for a certain time
        // but the estimated time is so long that it carries to the next day
        // I've done this so night owls can use the app the same way as everyone
        // else.
        //
        // E.g if I start something at 10:00pm and set the estimated time
        // for 8 hours, and I've already done 1 hour of tasks today, I shouldn't
        // be told to reschedule as there only is 2 more hours left in the day, not 7/8.
        //
        // Important part: for todos that span over days, split the time period
        // accordingly and place that duration into its corresponding date/day list

        Duration timeLeftInDay = timeLeftInDay(estimatedStart);
        durations = sortedTimes.computeIfAbsent(estStartDate, k -> new ArrayList<>());
        durations.add(timeLeftInDay);
        estimatedDuration = estimatedDuration.minus(timeLeftInDay);

        // recursion magic
        addTime(sortedTimes, getMidnightNextDay(estimatedStart), estimatedDuration);
    }

    private boolean overThresholdAnyDay(List<Todo> todos, Duration threshold) {
        Map<LocalDate, List<Duration>> sortedTimes = new HashMap<>();

        for (Todo todo : todos) {
            ZonedDateTime estimatedStart = todo.getEstimatedStart();
            Duration estimatedDuration = todo.getEstimatedTime();

            addTime(sortedTimes, estimatedStart, estimatedDuration);
        }

        for (LocalDate day : sortedTimes.keySet()) {
            Duration totalDailyTime = Duration.ZERO;

            for (Duration estmTaskLen : sortedTimes.get(day)) {
                totalDailyTime = totalDailyTime.plus(estmTaskLen);
                int compVal = totalDailyTime.compareTo(threshold);

                if (compVal > 0) {
                    return true;
                }
            }
        }

        return false;
    }

    private Mood randomPositiveMood() {
        int numPositiveMoods = normalPositiveMoods.size();

        // TODO : if all tasks are done for day, return euphoric

        return normalPositiveMoods.get((int) (Math.random() % numPositiveMoods));
    }

    public Boolean checkIfConcerned(User user) {
        List<Todo> todos = userTodoService.getTodoList(user);

        return anyTodoOverdue(todos) || overThresholdAnyDay(todos, dailyTreshold);
    }

    private Map<LocalDate, Set<Todo>> mapTodosToDay(Set<Todo> todos, ZoneId timeZone) {
        Map<LocalDate, Set<Todo>> tasksForEachDay = new HashMap<>();
        Set<Todo> dailyTodos;

        ZonedDateTime estCompleteDateTime;
        LocalDate estCompleteDate;

        for (Todo todo : todos) {
            if (!todo.isDone() && !todoOverdue(todo)) {
                ZonedDateTime estStart = todo.getEstimatedStart().withZoneSameInstant(timeZone);

                estCompleteDateTime  = estStart.plus(todo.getEstimatedTime());
                estCompleteDate = estCompleteDateTime.toLocalDate();
            } else if (todo.isDone()) {
                estCompleteDateTime = todo.getDateCompleted().withZoneSameInstant(timeZone);
                estCompleteDate = estCompleteDateTime.toLocalDate();
            } else {
                // it's overdue
                estCompleteDate = ZonedDateTime.now(timeZone).toLocalDate();
            }

            dailyTodos = tasksForEachDay.computeIfAbsent(estCompleteDate, k -> new HashSet<>());
            dailyTodos.add(todo);
        }

        return tasksForEachDay;
    }

    private Boolean tasksDoneForDay(Set<Todo> todos, LocalDate day, ZoneId timeZone) {
        Map<LocalDate, Set<Todo>> tasksForEachDay = mapTodosToDay(todos, timeZone);
        Set<Todo> dailyTodos;

        dailyTodos = tasksForEachDay.get(day);

        if (dailyTodos == null) {
            return true;
        }

        for (Todo todo : dailyTodos) {
            if (!todo.isDone()) {
                return false;
            }
        }

        return true;
    }

    public String moodForTodo(User user, Todo todo) {
        ZoneId commonTimeZone = ZoneId.systemDefault();
        Mood mood;

        if (todoOverdue(todo)) {
            mood = Mood.CONCERNED;
        } else if (todo.isDone()) {
            if (tasksDoneForDay(user.getTodos(),
                    todo.getDateCompleted().toLocalDate(),
                    commonTimeZone)) {
                // TODO : strengthen condition so
                // euphoric requires at least 3
                // todos in a day
                mood = Mood.EUPHORIC;
            } else {
                mood = randomPositiveMood();
            }
        } else {
            mood = Mood.NEUTRAL;
        }

        return mood.toString();
    }

    /**
     * Creates a new flamelet entity for a user and
     * returns it
     * @param user
     * @return the newly created flamelet
     */
    @Transactional
    public Flamelet createFlamelet(User user) {
        Flamelet flamelet = new Flamelet();

        flamelet.setUser(user);
        flamelet.setMood(Mood.NEUTRAL.name());

        return flameletRepository.save(flamelet);
    }
}
