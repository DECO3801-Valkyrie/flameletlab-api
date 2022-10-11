package com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.GroupChat;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.ChatTag;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.CreateGroupResponse;
import org.springframework.stereotype.Service;

@Service
public class ChatGroupMapper {

    public CreateGroupResponse chatGroupToCreateGroupResponse(GroupChat groupChat) {
        CreateGroupResponse createGroupResponse = new CreateGroupResponse();
        createGroupResponse.setName(groupChat.getName());
        createGroupResponse.setId(groupChat.getId());
        createGroupResponse.setCreated(groupChat.getCreated());
        createGroupResponse.setTags(groupChat.getTags().stream().map(ChatTag::getName).toList());

        return createGroupResponse;
    }
}
