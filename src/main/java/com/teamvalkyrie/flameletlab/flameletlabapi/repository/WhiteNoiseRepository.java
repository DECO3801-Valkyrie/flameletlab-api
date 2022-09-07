package com.teamvalkyrie.flameletlab.flameletlabapi.repository;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.WhiteNoise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WhiteNoiseRepository extends JpaRepository<WhiteNoise, Long> {

}
