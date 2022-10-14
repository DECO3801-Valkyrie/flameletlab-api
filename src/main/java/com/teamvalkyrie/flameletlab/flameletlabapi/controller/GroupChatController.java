package com.teamvalkyrie.flameletlab.flameletlabapi.controller;

import com.teamvalkyrie.flameletlab.flameletlabapi.repository.helper.ChatSearchCriteria;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.helper.ChatSearchSpecificationsBuilder;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.ChatService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.UserService;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.CreateGroupResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.CreateGroupRequest;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.GroupChatMessagesResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.GroupChatResponse;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.mapper.GroupChatMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GroupChatController {

    private final ChatService chatService;

    private final GroupChatMapper groupChatMapper;

    private final UserService userService;


    /**
     * {@code POST /group-chat} : create a new chat group
     *
     * @param groupRequest body containing new group info
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and the persisted group.
     */
    @PostMapping("/group-chat")
    public ResponseEntity<CreateGroupResponse> createGroup(
            @RequestBody @Valid CreateGroupRequest groupRequest) {
        var newGroup = chatService.createGroup(groupRequest);
       return ResponseEntity.created(URI.create("/api/group-chat/" + newGroup.getId()))
               .body(groupChatMapper.chatGroupToCreateGroupResponse(newGroup));
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

    /**
     * {@code GET /group-chat/:groupId/messages} : get the messages for the group specified
     *
     * @param groupId the group chat id to get messages for
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and body containing messages
     */
    @GetMapping("/group-chat/{groupId}/messages")
    public ResponseEntity<GroupChatMessagesResponse> getGroupChatMessage(@PathVariable Long groupId) {
        var groupChat = chatService.getGroupByIdWithMessages(groupId);
        return groupChat.map(gc -> {
            GroupChatMessagesResponse response = new GroupChatMessagesResponse();
            response.setGroupChat(groupChatMapper.groupChatToGroupChatResponse(gc));
            response.setMessages(groupChatMapper.groupChatMessagesToGroupChatMessagesResponse(gc.getMessages(),
                    userService.getCurrentLoggedInUser().getId()));
            return ResponseEntity.ok().body(response);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }


    /**
     * {@code GET /group-chat/ : get groups based on user search criteria

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and body containing groups
     */
    @GetMapping("/group-chat/")
    public ResponseEntity<List<GroupChatResponse>> getGroups(@RequestParam  Map<String, String> searchOptions) {
        searchOptions.put("userId", userService.getCurrentLoggedInUser().getId().toString());

        ChatSearchCriteria searchCriteria = new ChatSearchCriteria(searchOptions);
        ChatSearchSpecificationsBuilder chatSearchSpecificationsBuilder = new ChatSearchSpecificationsBuilder(searchCriteria);
        var searchSpec = chatSearchSpecificationsBuilder.build();
       return ResponseEntity.ok().body(chatService.getAllGroupChats(searchSpec).stream()
               .map(groupChatMapper::groupChatToGroupChatResponse).toList());
    }
}
