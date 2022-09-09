package com.teamvalkyrie.flameletlab.flameletlabapi.service;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.Flamelet;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.User;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.FlameletRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class FlameletService {
    private enum Mood {
        NEUTRAL,
        HAPPY,
        EXCITED,
        JOYFUL,
        EXHILARATED,
        EUPHORIC
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

    /**
     * Looks at the users todos and calculates the mood for flamelet based on them
     * @param user
     * @return flamelet's mood
     */
    private String calculateFlameletMood(User user) {
        Mood mood;

        int numTodos = userTodoService.getNumberOfTodos(user);
        double doneProportion = 0;

        if (numTodos != 0) {
            doneProportion = (float) userTodoService.getNumberOfDoneTodos(user) / numTodos;
        }

        /*
         * Follows a discrete linear relationship between the proportion of
         * todos done flamelet's mood
         * the moods are ordered from least to greatest as follows:
         * {"neutral", "happy", "excited", "joyful", "exhilarated", "euphoric"}
         * from moods neutral-exhilarated it occupies one quintile (20%) according
         * to the ordering (neutral goes from 0 to 0.19999....)
         *
         * "euphoric" is the upper bound and is only achieved iff all tasks are done
         *
         * the todoThreshold makes it so iff the number of todos < threshold, the highest
         * mood flamelet can be is "excited"
         */
        if (0.2 <= doneProportion && doneProportion < 0.4) {
            mood = Mood.HAPPY;
        } else if ((0.4 <= doneProportion && doneProportion < 0.6) ||
                (doneProportion == 1 && numTodos < todoThreshold)) {
            mood = Mood.EXCITED;
        } else if (0.6 <= doneProportion && doneProportion < 0.8) {
            mood = Mood.JOYFUL;
        } else if (0.8 <= doneProportion && doneProportion < 1) {
            mood = Mood.EXHILARATED;
        } else if (doneProportion == 1) {
            mood = Mood.EUPHORIC;
        } else {
            mood = Mood.NEUTRAL;
        }

        return mood.toString().toLowerCase();
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
        return calculateFlameletMood(user);
    }

}
