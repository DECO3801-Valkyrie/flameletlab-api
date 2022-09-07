package com.teamvalkyrie.flameletlab.flameletlabapi.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.util.Objects;

@Entity
@Getter
@Setter
@Table()
@NoArgsConstructor
public class UserWhiteNoise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "white_noise_id",referencedColumnName = "id")
    private WhiteNoise whiteNoise;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private ZonedDateTime created;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        UserWhiteNoise that = (UserWhiteNoise) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
