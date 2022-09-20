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

    private static final List<Mood> positiveMoods = initPositiveMoods();

    private static List<Mood> initPositiveMoods() {
        Mood[] positiveMoods = {Mood.HAPPY, Mood.EXCITED, Mood.JOYFUL, Mood.EXHILARATED, Mood.EUPHORIC};

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
        ZonedDateTime currTime = ZonedDateTime.from(Instant.now());

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

    private OffsetDateTime getMidnightNextDay(OffsetDateTime dateTime) {
        ZonedDateTime midnightNextDay = dateTime.plusDays(1).toLocalDate().
                atStartOfDay(dateTime.getOffset());

        return midnightNextDay.toOffsetDateTime();

    }

    private Duration timeLeftInDay(OffsetDateTime dateTime) {
        // get the dateTime for the day tommorow, then roll it back to
        // midnight
        OffsetDateTime midnightNextDay = getMidnightNextDay(dateTime);

        return Duration.between(dateTime, midnightNextDay);
    }

    private void addTime(Map<LocalDate, List<Duration>> sortedTimes,
                          OffsetDateTime estimatedStart, Duration estimatedDuration) {
        OffsetDateTime estimatedFinish = estimatedStart.plus(estimatedDuration);
        LocalDate estFinishDate = estimatedFinish.toLocalDate();
        LocalDate estStartDate = estimatedStart.toLocalDate();

        if (estFinishDate == estStartDate) {
            sortedTimes.get(estFinishDate).add(estimatedDuration);
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
        sortedTimes.get(estStartDate).add(timeLeftInDay);
        estimatedDuration = estimatedDuration.minus(timeLeftInDay);

        // recursion magic
        addTime(sortedTimes, getMidnightNextDay(estimatedStart), estimatedDuration);
    }

    private boolean overThresholdAnyDay(List<Todo> todos, Duration threshold) {
        Map<LocalDate, List<Duration>> sortedTimes = new HashMap<>();

        for (Todo todo : todos) {
            OffsetDateTime estimatedStart = todo.getEstimatedStart().toOffsetDateTime();
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
        int numPositiveMoods = positiveMoods.size();

        return positiveMoods.get((int) (Math.random() % numPositiveMoods));
    }

    /**
     * Looks at the users todos and calculates the mood for flamelet based on them
     * @param user
     * @return flamelet's mood
     */
    private String calculateFlameletMood(User user, Duration threshold) {
        Mood mood;
        List<Todo> todos = userTodoService.getTodoList(user);

        if (anyTodoOverdue(todos) || overThresholdAnyDay(todos, threshold)) {
            mood = Mood.CONCERNED;
        } else {
            // TODO : still need to work on this
            // need to change/get a positive mood
            // each time a task is completed
            mood = randomPositiveMood();
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

    /**
     * Gets the current mood of a user's flamelet.
     * @param user
     * @return user's flamelet's mood
     */
    public String getMood(User user) {
        //String mood;
        //setMood(user); // updates flamelet's mood
        //mood = getUsersFlamelet(user).getMood();

        // mood doesn't need to be stored in DB, just
        // calculate when it's needed
        return calculateFlameletMood(user, dailyTreshold);
    }
}
