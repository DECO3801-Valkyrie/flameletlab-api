package com.teamvalkyrie.flameletlab.flameletlabapi.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "anonymous_group_chat_user")
@Getter
@Setter
public class AnonymousGroupChatUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "group_chat_id", referencedColumnName = "id", nullable = false)
    private GroupChat groupChat;

    @Column
    private String anonymousName;

    @Column
    private String anonymousImage;
}
