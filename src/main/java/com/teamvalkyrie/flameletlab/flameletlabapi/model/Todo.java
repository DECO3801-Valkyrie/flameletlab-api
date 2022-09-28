package com.teamvalkyrie.flameletlab.flameletlabapi.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Table
@Getter
@Setter
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private Boolean done;

    @Column
    private ZonedDateTime created;

    @Column
    private ZonedDateTime dateCompleted;

    @Column
    private Duration estimatedTime;

    @Column
    private ZonedDateTime estimatedStart;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Todo todo = (Todo) o;
        return id != null && Objects.equals(id, todo.id);
    }

    public boolean isDone() {
        return done;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
