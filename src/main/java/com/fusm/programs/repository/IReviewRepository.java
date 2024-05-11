package com.fusm.programs.repository;

import com.fusm.programs.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IReviewRepository extends JpaRepository<Review, Integer> {

    @Query(
            value = "SELECT * FROM review " +
                    "where object_id = :objectId " +
                    "and object_type = :objectType " +
                    "order by created_at asc ",
            nativeQuery = true
    )
    List<Review> findAllByObjectIdAndObjectType(
            @Param("objectId") Integer objectId,
            @Param("objectType") Integer objectType
    );

}
