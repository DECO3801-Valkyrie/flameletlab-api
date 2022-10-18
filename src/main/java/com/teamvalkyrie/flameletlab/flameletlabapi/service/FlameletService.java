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
        EUPHORIC,
        STAYSAME
    }

    private static final Duration dailyTreshold = initDailyTreshold();

    private static Duration initDailyTreshold() {
        int hours = 8;

        return Duration.ofHours(hours);
    }

    private static final List<Mood> normalPositiveMoods = initNormalPositiveMoods();

    /**
     * Find helper function to initialise flamelet's list of positive moods
     * @return list of positive moods
     */
    private static List<Mood> initNormalPositiveMoods() {
        Mood[] positiveMoods = {Mood.HAPPY, Mood.EXCITED, Mood.JOYFUL, Mood.EXHILARATED};

        return new ArrayList<>(Arrays.asList(positiveMoods));
    }

    private final UserTodoService userTodoService;

    private final FlameletRepository flameletRepository;

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

    /**
     * Helper method to check if a todo is overdue. A todo is overdue
     * iff the current time is past its estimated completion time
     * @param todo
     * @return true iff the todo is overdue, else false
     */
    private boolean todoOverdue(Todo todo) {
        if (todo.getDateCompleted() != null) {
            return false;
        }

        Duration estimatedTime = todo.getEstimatedTime();
        ZonedDateTime dueByTime = todo.getCreated().plus(estimatedTime);
        ZonedDateTime currTime = ZonedDateTime.now(dueByTime.getZone());

        return currTime.isAfter(dueByTime);
    }

    /**
     * Checks if there exists a todo in the list of todos that is
     * overdue. A todo is overdue iff the current time is past its
     * estimated completion time
     * @param todos
     * @return
     */
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

    /**
     * Helper method that gets the timezone sensitive date
     * that is midnight the next day after the given datetime
     * @param dateTime
     * @return datetime that represents midnight the next day after
     * the given datetime
     */
    private ZonedDateTime getMidnightNextDay(ZonedDateTime dateTime) {

        return dateTime.plusDays(1).toLocalDate().
                atStartOfDay(dateTime.getZone());

    }

    /**
     * Finds the amount of time left in a day given a datetime.
     * @param dateTime day and time of day
     * @return the amount of time left in the specified day
     */
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

    /**
     * Checks if a user has added too many todos tasks in a single/any day.
     * The threshold is the total maximum estimated time of todo tasks a user
     * can have in a single day. Basically if the total estimated time of the todos
     * planned to be done in a single is exceeded, flamelet is not too happy.
     * @param todos all the todos to be checked
     * @param threshold daily threshold
     * @return true if the threshold has been breached in any day, else false
     */
    private boolean overThresholdAnyDay(List<Todo> todos, Duration threshold) {
        Map<LocalDate, List<Duration>> sortedTimes = new HashMap<>();

        // Sort the todos into their days that they belong to
        for (Todo todo : todos) {
            ZonedDateTime estimatedStart = todo.getEstimatedStart();
            Duration estimatedDuration = todo.getEstimatedTime();

            addTime(sortedTimes, estimatedStart, estimatedDuration);
        }

        // For each day, check if the threshold gets breached
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

    /**
     * Get a random positive mood
     * @return a random positive mood
     */
    private Mood randomPositiveMood() {
        int numPositiveMoods = normalPositiveMoods.size();

        return normalPositiveMoods.get((int) (Math.random() % numPositiveMoods));
    }

    /**
     * Checks if flamelet is concerned. Flamelet is concerned iff any of the user's
     * todos are overdue in a day or that the user has planned out too many
     * todo tasks for a single day.
     * @param user
     * @return true iff concerned else false
     */
    public Boolean checkIfConcerned(User user) {
        List<Todo> todos = userTodoService.getTodoList(user);

        return anyTodoOverdue(todos) || overThresholdAnyDay(todos, dailyTreshold);
    }

    /**
     * Helper function that maps todos to their days.
     * @param todos to be mapped
     * @param timeZone the timezone of the days (i.e Monday AEST).
     *                 Required for time-date accuracy
     * @return
     */
    private Map<LocalDate, Set<Todo>> mapTodosToDay(Set<Todo> todos, ZoneId timeZone) {
        Map<LocalDate, Set<Todo>> tasksForEachDay = new HashMap<>();
        Set<Todo> dailyTodos;

        ZonedDateTime estCompleteDateTime;
        LocalDate estCompleteDate;

        for (Todo todo : todos) {
            if (!todo.isDone() && !todoOverdue(todo)) {
                // todo belongs to the day where it's estimated to be done
                ZonedDateTime estStart = todo.getEstimatedStart().withZoneSameInstant(timeZone);

                estCompleteDateTime  = estStart.plus(todo.getEstimatedTime());
                estCompleteDate = estCompleteDateTime.toLocalDate();
            } else if (todo.isDone()) {
                // todo belongs to the day where it was done
                estCompleteDateTime = todo.getDateCompleted().withZoneSameInstant(timeZone);
                estCompleteDate = estCompleteDateTime.toLocalDate();
            } else {
                // it's overdue, todo belongs to today
                estCompleteDate = ZonedDateTime.now(timeZone).toLocalDate();
            }

            // get the day's todo set
            dailyTodos = tasksForEachDay.computeIfAbsent(estCompleteDate, k -> new HashSet<>());
            dailyTodos.add(todo);
        }

        return tasksForEachDay;
    }

    /**
     * Checks if all tasks are done for a specific day
     * @param todos
     * @param day
     * @param timeZone of the day
     * @return true iff all are completed, else false
     */
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

    /**
     * Give a user and a todo, returns what flamelet thinks of it, i.e is mood.
     * If the todo is overdue, flamelet will be concernced, else a positive mood
     * will be returned.
     * @param user
     * @param todo
     * @return concerned if the task overdue. If all tasks are done in the todo's day,
     * flamelet will be EUPHORIC. If this task and some others are done in the todo's day,
     * a random positive mood besides euphoric will be returned. If no tasks are done in the
     * todo's day, flamelet will be NEUTRAL. Else flamelet's mood will stay the same as how it was
     * before (STAYSAME is returned)
     */
    public String moodForTodo(User user, Todo todo) {
        ZoneId commonTimeZone = ZoneId.systemDefault();
        Mood mood;

        if (todoOverdue(todo)) {
            mood = Mood.CONCERNED;
        } else if (todo.isDone()) {
            LocalDate doneDay = todo.getDateCompleted().toLocalDate();
            ZoneId doneTimeZone = todo.getDateCompleted().getZone();
            long numDoneTasksForDay = userTodoService.getNumberDoneTodosForDay(user, doneDay, doneTimeZone);

            if (tasksDoneForDay(user.getTodos(),
                    todo.getDateCompleted().toLocalDate(),
                    commonTimeZone) && numDoneTasksForDay > 3) {
                // all are done and a there is enough done to warrant a
                // euphoric mood
                mood = Mood.EUPHORIC;
            } else {
                mood = randomPositiveMood();
            }
        } else if (userTodoService.getNumberOfDoneTodos(user) == 0) {
            mood = Mood.NEUTRAL;
        } else {
            mood = Mood.STAYSAME;
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

        return flameletRepository.saveAndFlush(flamelet);
    }
}
