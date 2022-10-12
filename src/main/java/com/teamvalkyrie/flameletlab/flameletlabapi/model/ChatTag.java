package com.teamvalkyrie.flameletlab.flameletlabapi.model;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
@Getter
@Setter
public class ChatTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ChatTag chatTag = (ChatTag) o;
        return id != null && Objects.equals(id, chatTag.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
