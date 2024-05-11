package com.fusm.programs.repository;

import com.fusm.programs.entity.Campus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ICampusRepository extends JpaRepository<Campus, Integer> {

    List<Campus> findAllByProgramId_ProgramIdAndEnabled(Integer programId, Boolean enabled);

    List<Campus> findAllByValueAndProgramId_ProgramId(Integer value, Integer programId);

    @Query(
            value = "SELECT * " +
                    "FROM campus " +
                    "WHERE campus.program_id = :programId " +
                    "AND campus.value NOT IN (:campusId)",
            nativeQuery = true
    )
    List<Campus> findCampusNotIncluded(
            @Param("campusId") Integer[] campusId,
            @Param("programId") Integer programId);

}
