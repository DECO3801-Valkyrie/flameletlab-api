package com.teamvalkyrie.flameletlab.flameletlabapi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
@EqualsAndHashCode(of = {"occupation_type"})

public class occupation_type {
    @Id
    private Long id;

    private String name;
}

