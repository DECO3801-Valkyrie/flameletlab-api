package com.teamvalkyrie.flameletlab.flameletlabapi.repository;

import com.teamvalkyrie.flameletlab.flameletlabapi.model.WorkplaceRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkplaceRatingRepository extends JpaRepository<WorkplaceRating, Long> {
    @Query(
    """
    select wr from WorkplaceRating wr join Workplace w on w.id=wr.workplace.id
    where w.placeId = :placeId
    """)
    List<WorkplaceRating> findAllByPlaceId(@Param("placeId") String placeId);

}
