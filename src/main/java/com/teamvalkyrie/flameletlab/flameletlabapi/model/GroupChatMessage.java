package com.teamvalkyrie.flameletlab.flameletlabapi.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Table(name = "group_chat_message")
@Getter
@Setter
public class GroupChatMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String message;

    @Column
    @NotNull
    private ZonedDateTime created;

    @ManyToOne
    @JoinColumn(name = "anonymous_group_chat_user_id", referencedColumnName = "id", nullable = false)
    private AnonymousGroupChatUser anonUser;

    @ManyToOne
    @JoinColumn(name = "group_chat_id", referencedColumnName = "id", nullable = false)
    private GroupChat groupChat;
}
