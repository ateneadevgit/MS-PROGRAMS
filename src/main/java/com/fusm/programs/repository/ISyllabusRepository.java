package com.fusm.programs.repository;

import com.fusm.programs.entity.Syllabus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ISyllabusRepository extends JpaRepository<Syllabus, Integer> {

    @Query(
            value = "SELECT * " +
                    "FROM public.syllabus " +
                    "join workflow_base_step " +
                    "on workflow_base_step.workflow_base_step_id = syllabus.workflow_base_step_id " +
                    "join workflow_base " +
                    "on workflow_base.id_workflow_base = workflow_base_step.workflow_base_id " +
                    "where workflow_base.workflow_object_id = :programId ",
            nativeQuery = true
    )
    List<Syllabus> findSyllabusByProgram(@Param("programId") Integer programId);

}
