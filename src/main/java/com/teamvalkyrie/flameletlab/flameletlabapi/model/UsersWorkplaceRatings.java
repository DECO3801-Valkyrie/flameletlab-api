package com.teamvalkyrie.flameletlab.flameletlabapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.w3c.dom.Text;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
@NoArgsConstructor
@Getter
@Setter

public class UsersWorkplaceRatings {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "workplace_id", referencedColumnName = "id")
    private Workplace workplace;

    @Column
    private String review;

    @Column
    private Float rating;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UsersWorkplaceRatings that = (UsersWorkplaceRatings) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
