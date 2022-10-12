package com.teamvalkyrie.flameletlab.flameletlabapi.controller;

import com.teamvalkyrie.flameletlab.flameletlabapi.service.ChatService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.AnonymousUserMessageResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.GroupChatMessageResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.GroupChatMessagesResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.WebSocketMessage;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper.GroupChatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
public class MessagingController {

    private final ChatService chatService;

    private final GroupChatMapper groupChatMapper;

    @MessageMapping("/message/{groupId}")
    @SendTo("/group/{groupId}")
    public GroupChatMessageResponse message(@DestinationVariable Long groupId, WebSocketMessage message) {
        var newMessage = chatService.saveMessageForGroup(groupId, message);
        return new GroupChatMessageResponse(newMessage.getId(),
                newMessage.getAnonymousUser().getAnonymousName(),
                newMessage.getAnonymousUser().getAnonymousImage(),
                newMessage.getMessage(),
                newMessage.getCreated());

    }

}