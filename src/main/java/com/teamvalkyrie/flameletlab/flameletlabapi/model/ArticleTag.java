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
public class ArticleTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String tagName;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ArticleTag articleTag = (ArticleTag) o;
        return id != null && Objects.equals(id, articleTag.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
