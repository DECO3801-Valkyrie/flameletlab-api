package com.teamvalkyrie.flameletlab.flameletlabapi.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GroupChatMessagesResponse {
    GroupChatResponse groupChat;
    List<GroupChatMessageResponse> messages;
}

