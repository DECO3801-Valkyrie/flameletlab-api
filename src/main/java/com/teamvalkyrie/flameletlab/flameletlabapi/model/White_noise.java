package com.teamvalkyrie.flameletlab.flameletlabapi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;


@Entity
@Data
@EqualsAndHashCode(of = {"white_noise"})

public class White_noise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String pictures;

    @Column
    private Long listens;

    @Column
    private String audio_path;

}
