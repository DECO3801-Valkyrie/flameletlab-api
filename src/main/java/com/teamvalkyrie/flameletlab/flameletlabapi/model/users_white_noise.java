package com.teamvalkyrie.flameletlab.flameletlabapi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@EqualsAndHashCode(of = {"users-white-noise"})
public class users_white_noise {
    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "white_noise_id", referencedColumnName = "id")
    private White_noise white_noise;

    @Id
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private LocalDate created;


}
