package com.teamvalkyrie.flameletlab.flameletlabapi.model;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.enumeration.RoleType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(of = {"role"})
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleType role;

    public Role(RoleType role) {
        this.role = role;
    }
}