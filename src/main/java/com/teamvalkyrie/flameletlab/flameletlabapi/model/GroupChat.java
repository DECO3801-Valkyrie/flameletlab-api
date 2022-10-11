package com.teamvalkyrie.flameletlab.flameletlabapi.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
public class GroupChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Integer totalUsers;

    @OneToOne
    @JoinColumn(name = "occupation_type_id", referencedColumnName =  "id")
    private OccupationType occupationType;

    @Column
    private ZonedDateTime created;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "group_chat_tag",
            joinColumns = @JoinColumn(
                    name = "group_chat_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "chat_tag_id", referencedColumnName = "id"))
    private Set<ChatTag> tags = new HashSet<>();

    @OneToMany(mappedBy = "group")
    private Set<AnonymousGroupChatUser> anonymousUsers = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GroupChat groupChat = (GroupChat) o;
        return id != null && Objects.equals(id, groupChat.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
