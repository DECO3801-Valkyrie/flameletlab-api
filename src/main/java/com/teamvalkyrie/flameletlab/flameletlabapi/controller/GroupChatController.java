package com.teamvalkyrie.flameletlab.flameletlabapi.controller;

import com.teamvalkyrie.flameletlab.flameletlabapi.service.ChatService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.CreateGroupResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.CreateGroupRequest;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper.ChatGroupMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GroupChatController {

    private final ChatService chatService;

    private final ChatGroupMapper chatGroupMapper;

    /**
     * {@code POST /group-chat} : create a new chat group
     *
     * @param groupRequest body containing new group info
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the persisted group.
     */
    @PostMapping("/group-chat")
    public ResponseEntity<CreateGroupResponse> createGroup(
            @RequestBody @Valid CreateGroupRequest groupRequest) {
        var newGroup = chatService.createGroup(groupRequest);
       return ResponseEntity.created(URI.create("/api/group-chat/" + newGroup.getId()))
               .body(chatGroupMapper.chatGroupToCreateGroupResponse(newGroup));
    }

    /**
     * {@code POST /group-chat/:groupId/join} : current user join a chat
     *
     * @param groupId the group chat id to join
     * @return the {@link ResponseEntity} with status {@code 200 (OK)}
     */
    @PostMapping("/group-chat/{groupId}/join")
    public ResponseEntity<?> join(@PathVariable Long groupId) {
        chatService.joinGroup(groupId);
        return ResponseEntity.ok().body("");
    }
}
