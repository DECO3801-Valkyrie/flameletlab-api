package com.teamvalkyrie.flameletlab.flameletlabapi.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
public class CachedArticle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String url;

    @Lob
    @Column
    private String urlToImage;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "cached_article_tag_pair",
            joinColumns = @JoinColumn(
                    name = "article_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(
                    name = "article_tag_id", referencedColumnName = "id"))
    private Set<ArticleTag> tags;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        CachedArticle cachedArticle = (CachedArticle) o;
        return id != null && Objects.equals(id, cachedArticle.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
