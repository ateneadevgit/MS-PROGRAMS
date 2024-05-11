package com.fusm.programs.repository;

import com.fusm.programs.entity.CurriculumSummary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICurriculumSummaryRepository extends JpaRepository<CurriculumSummary, Integer> {

    @Query(
            value = "SELECT * " +
                    "FROM curriculum_summary " +
                    "join workflow_base_step " +
                    "on workflow_base_step.workflow_base_step_id = curriculum_summary.workflow_base_step_id " +
                    "join workflow_base " +
                    "on workflow_base.id_workflow_base = workflow_base_step.workflow_base_id " +
                    "where workflow_base.workflow_object_id = :programId " +
                    "and curriculum_summary.curriculum_type = :type ",
            nativeQuery = true
    )
    List<CurriculumSummary> findCurriculumSummaryByProgramAndType(
            @Param("programId") Integer programId,
            @Param("type") Integer type
    );

}
