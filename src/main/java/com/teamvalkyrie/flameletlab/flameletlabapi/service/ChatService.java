package com.teamvalkyrie.flameletlab.flameletlabapi.service;

import com.teamvalkyrie.flameletlab.flameletlabapi.constant.Animals;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.*;
import com.teamvalkyrie.flameletlab.flameletlabapi.model.misc.Animal;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.GroupChatRepository;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.ChatTagRepository;
import com.teamvalkyrie.flameletlab.flameletlabapi.repository.OccupationTypeRepository;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.dto.CreateGroupRequest;
import com.teamvalkyrie.flameletlab.flameletlabapi.service.exception.GroupChatException;
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

    private final UserService userService;


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
    @Transactional(rollbackFor = GroupChatException.class)
    public GroupChat createGroup(CreateGroupRequest request) {
        GroupChat newGroup = new GroupChat();
        newGroup.setName(request.getName());
        newGroup.setCreated(ZonedDateTime.now());
        Optional<OccupationType> occupationType = occupationTypeRepository.findById(request.getOccupationTypeId());
        if(occupationType.isEmpty()) {
            throw new GroupChatException("Unable to create group, occupation type is wrong");
        }
        newGroup.setTotalUsers(1);
        newGroup.setOccupationType(occupationType.get());
        newGroup.setTags(createNewTags(request.getTags()));



        var newSavedGroup =  groupChatRepository.saveAndFlush(newGroup);
        // Join owner of group
        joinGroup(newSavedGroup.getId());

        return newSavedGroup;
     }


     private AnonymousGroupChatUser getNewAnonymousUserForGroup(Long groupChatId) {

        var groupChatOptional = groupChatRepository.findAllByIdWithUsers(groupChatId);
        if (groupChatOptional.isPresent()) {
           var users = groupChatOptional.get().getAnonymousUsers();

            /*
             * @TODO there may be a case where all anonymous user names
             * are taken in that case a hash can be appended such as
             * Anonymous Donkey #2
             *
             * This can be done a in a future release
             */
            for(Animal animal : Animals.getAnimals()) {
               String newAnonymousName = "Anonymous " + animal;
               if (users.stream().map(AnonymousGroupChatUser::getAnonymousName)
                       .noneMatch(n -> n.equalsIgnoreCase(newAnonymousName))) {
                   return new AnonymousGroupChatUser(null, userService.getCurrentLoggedInUser(),
                           groupChatOptional.get(), newAnonymousName, animal.getImage());
               }
           }
        }

        return null;
     }

    /**
     * The current logged-in user is joining a group chat
     *
     * @param groupChatId the id of the group to join
     */
    @Transactional
     public void joinGroup(Long groupChatId) {
        Optional<GroupChat> groupChatOptional = groupChatRepository.findById(groupChatId);
        GroupChat groupChat = null;
        if (groupChatOptional.isEmpty()) {
            throw new GroupChatException("Unable to find group");
        } else {
            groupChat = groupChatOptional.get();
        }
        var newAnonymousUser = getNewAnonymousUserForGroup(groupChatId);
        if (newAnonymousUser == null) {
            throw new GroupChatException("Unable to create anonymous name");
        }

        groupChat.setTotalUsers(groupChat.getTotalUsers() + 1);
     }


}
