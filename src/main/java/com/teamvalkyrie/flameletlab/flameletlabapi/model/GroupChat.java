package com.teamvalkyrie.flameletlab.flameletlabapi.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "group_chat")
@SecondaryTable(name = "group_chat_tag", pkJoinColumns = @PrimaryKeyJoinColumn(name = "group_chat_id"))
@Getter
@Setter
public class GroupChat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String name;

    @OneToOne
    @JoinColumn(name = "occupation_type_id", referencedColumnName = "id", nullable = false)
    private OccupationType occupationType;

    @Column
    @NotNull
    private Long totalUsers;

    @Column
    @NotNull
    private ZonedDateTime created;

    @ManyToMany
    @JoinColumn(name = "chat_tag_id", referencedColumnName = "id", nullable = false)
    private List<ChatTag> tags;
}
