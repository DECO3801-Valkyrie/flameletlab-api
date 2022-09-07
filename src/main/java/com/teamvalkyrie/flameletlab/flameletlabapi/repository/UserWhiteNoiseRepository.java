package com.teamvalkyrie.flameletlab.flameletlabapi.repository;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.UserWhiteNoise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserWhiteNoiseRepository extends JpaRepository<UserWhiteNoise, Long> {
}
