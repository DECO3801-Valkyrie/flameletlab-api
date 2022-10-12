package com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.GroupChat;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.ChatTag;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.GroupChatMessage;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.CreateGroupResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.GroupChatMessageResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.GroupChatResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class GroupChatMapper {

    public CreateGroupResponse chatGroupToCreateGroupResponse(GroupChat groupChat) {
        CreateGroupResponse createGroupResponse = new CreateGroupResponse();
        createGroupResponse.setName(groupChat.getName());
        createGroupResponse.setId(groupChat.getId());
        createGroupResponse.setCreated(groupChat.getCreated());
        createGroupResponse.setTags(groupChat.getTags().stream().map(ChatTag::getName).toList());

        return createGroupResponse;
    }

    public GroupChatResponse groupChatToGroupChatResponse(GroupChat groupChat) {
        GroupChatResponse groupChatResponse = new GroupChatResponse();
        groupChatResponse.setId(groupChat.getId());
        groupChatResponse.setName(groupChat.getName());
        groupChatResponse.setOccupationType(groupChat.getOccupationType().getName());
        groupChatResponse.setTags(groupChat.getTags().stream().map(ChatTag::getName).toList());
        groupChatResponse.setTotalUsers(groupChat.getTotalUsers());

        return  groupChatResponse;
    }

    public List<GroupChatMessageResponse> groupChatMessagesToGroupChatMessagesResponse(Set<GroupChatMessage> messages, Long userId) {
       return  messages.stream().map(m -> new GroupChatMessageResponse(m.getId(),
               m.getAnonymousUser().getAnonymousName(),
               m.getAnonymousUser().getAnonymousImage(),
               m.getMessage(),
               m.getCreated(), m.getAnonymousUser().getUser().getId().equals(userId))).toList();
    }
}
