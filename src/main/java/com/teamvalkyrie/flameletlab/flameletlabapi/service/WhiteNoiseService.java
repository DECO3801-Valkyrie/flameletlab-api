package com.teamvalkyrie.flameletlab.flameletlabapi.service;


import com.teamvalkyrie.flameletlab.flameletlabapi.model.WhiteNoise;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.WhiteNoiseRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
@AllArgsConstructor
@Service
public class WhiteNoiseService {

    private final WhiteNoiseRepository whiteNoiseRepository;


    /**
     * Gets and returns a list of white noise in the system
     *
     * @return list of white noises
     */
    public List<WhiteNoise> getAll() {
        return this.whiteNoiseRepository.findAll();
    }

}
