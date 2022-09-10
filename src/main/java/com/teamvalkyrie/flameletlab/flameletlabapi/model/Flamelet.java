package com.teamvalkyrie.flameletlab.flameletlabapi.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "user_flamelet")
@Getter
@Setter
public class Flamelet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String mood;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Flamelet flamelet = (Flamelet) o;

        return id != null && Objects.equals(id, flamelet.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
