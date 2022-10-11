package com.teamvalkyrie.flameletlab.flameletlabapi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "chat_tag")
@SecondaryTable(name = "group_chat_tag", pkJoinColumns = @PrimaryKeyJoinColumn(name = "chat_tag_id"))
@Getter
@Setter
public class ChatTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @ManyToMany
    @JoinColumn(name = "group_chat_id", referencedColumnName = "id", nullable = false)
    private List<GroupChat> groupChats;
}
