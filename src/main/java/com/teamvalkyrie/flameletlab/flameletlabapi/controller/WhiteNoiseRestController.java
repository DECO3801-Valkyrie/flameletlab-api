package com.teamvalkyrie.flameletlab.flameletlabapi.controller;


import com.teamvalkyrie.flameletlab.flameletlabapi.model.WhiteNoise;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.WhiteNoiseService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.WhiteNoiseResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper.WhiteNoiseMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api")
@AllArgsConstructor
@RestController
public class WhiteNoiseRestController {

    private final WhiteNoiseService whiteNoiseService;
    private final WhiteNoiseMapper whiteNoiseMapper;

    /**
     * {@code GET  /white-noise} : get all the white noise.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of white noise in body.
     */
    @GetMapping("/white-noise")
    public ResponseEntity<List<WhiteNoiseResponse>> getAll() {
        var results =   whiteNoiseService.getAll()
                .stream().map(whiteNoiseMapper::whiteNoiseToWhiteNoiseResponse).toList();
        return ResponseEntity.ok().body(results);
    }

}
