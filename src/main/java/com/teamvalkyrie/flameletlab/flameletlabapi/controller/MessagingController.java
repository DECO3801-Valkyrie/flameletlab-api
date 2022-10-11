package com.teamvalkyrie.flameletlab.flameletlabapi.controller;

import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.AnonymousUserMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class MessagingController {


    @MessageMapping("/message/{groupId}")
    @SendTo("/group/{groupId}")
    public AnonymousUserMessageResponse message(@DestinationVariable String groupId, AnonymousUserMessageResponse message) {

    /*    message.setTone(ibmToneAnalyzerService.getMessageTone(message.getMessage()));
        message.setSent(LocalDateTime.now());
        roomService.saveMessageForRoom(roomId, message);*/
        return message;
    }
}