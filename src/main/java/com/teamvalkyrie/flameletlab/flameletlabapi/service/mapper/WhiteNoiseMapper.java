package com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper;


import com.teamvalkyrie.flameletlab.flameletlabapi.model.WhiteNoise;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.WhiteNoiseResponse;
import org.springframework.stereotype.Service;

@Service
public class WhiteNoiseMapper {

    public WhiteNoiseResponse whiteNoiseToWhiteNoiseResponse(WhiteNoise whiteNoise) {
        WhiteNoiseResponse wr = new WhiteNoiseResponse();
        wr.setId(whiteNoise.getId());
        wr.setListens(whiteNoise.getListens());
        wr.setLength(whiteNoise.getLength());
        wr.setTitle(whiteNoise.getTitle());
        wr.setPicture(whiteNoise.getPicture());
        wr.setAudioPath(whiteNoise.getAudioPath());

        return wr;
    }
}
