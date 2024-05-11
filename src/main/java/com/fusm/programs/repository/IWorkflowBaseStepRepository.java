package com.fusm.programs.repository;

import com.fusm.programs.entity.WorkflowBaseStep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IWorkflowBaseStepRepository extends JpaRepository<WorkflowBaseStep, Integer> {

    @Query(
            value = "SELECT * " +
                    "FROM workflow_base_step " +
                    "join workflow_base " +
                    "on workflow_base.id_workflow_base = workflow_base_step.workflow_base_id " +
                    "where workflow_base.workflow_object_id = :objectId " +
                    "and workflow_base.workflow_id = 2 ",
            nativeQuery = true
    )
    List<WorkflowBaseStep> findWorkflowBaseStepByObjectNative(@Param("objectId") Integer objectId);

}
