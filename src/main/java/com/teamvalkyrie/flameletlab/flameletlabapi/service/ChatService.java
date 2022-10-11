package com.teamvalkyrie.flameletlab.flameletlabapi.service;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.GroupChat;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.ChatTag;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.OccupationType;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.GroupChatRepository;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.ChatTagRepository;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.OccupationTypeRepository;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.CreateGroupRequest;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.exception.ChatGroupException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Transactional(readOnly = true)
@Service
@AllArgsConstructor
public class ChatService {

    private final GroupChatRepository groupChatRepository;
    private final OccupationTypeRepository occupationTypeRepository;

    private final ChatTagRepository chatTagRepository;

    /**
     * Creates new tags that the user has specified
     *
     * @param tags the tags containing new tags if any
     * @return new chat tags
     */
    private Set<ChatTag> createNewTags(List<String> tags) {
        List<ChatTag> allTags = chatTagRepository.findAll();
        Set<ChatTag> persistedTags = new HashSet<>();

        tags.forEach(t -> {
            if (allTags.stream().map(ChatTag::getName).noneMatch(ct -> ct.equalsIgnoreCase(t))) {
                ChatTag newChatTag = new ChatTag();
                newChatTag.setName(t.toLowerCase());
                persistedTags.add(chatTagRepository.save(newChatTag));
            } else {
                persistedTags.add(allTags.stream().filter(ct -> ct.getName().equalsIgnoreCase(t)).findFirst().get());
            }
        });


        return persistedTags;
    }

    /**
     * Creates a new group chat
     * @param request group details
     */
    @Transactional(rollbackFor = ChatGroupException.class)
    public GroupChat createGroup(CreateGroupRequest request) {
        GroupChat newGroup = new GroupChat();
        newGroup.setName(request.getName());
        newGroup.setCreated(ZonedDateTime.now());
        Optional<OccupationType> occupationType = occupationTypeRepository.findById(request.getOccupationTypeId());
        if(occupationType.isEmpty()) {
            throw new ChatGroupException("Unable to create group, occupation type is wrong");
        }
        newGroup.setTotalUsers(1);
        newGroup.setOccupationType(occupationType.get());
        newGroup.setTags(createNewTags(request.getTags()));

        // Join owner of group

        return groupChatRepository.saveAndFlush(newGroup);
     }

     public void createNewAnonymousUserForCurrentLoggedInUser(GroupChat groupChat) {


     }


}
