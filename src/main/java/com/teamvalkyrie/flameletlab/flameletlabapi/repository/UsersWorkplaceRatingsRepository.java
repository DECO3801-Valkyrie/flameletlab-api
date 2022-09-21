package com.teamvalkyrie.flameletlab.flameletlabapi.repository;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.UsersWorkplaceRatings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersWorkplaceRatingsRepository extends JpaRepository<UsersWorkplaceRatings, Long> {

}
