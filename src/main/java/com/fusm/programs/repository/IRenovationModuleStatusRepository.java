package com.fusm.programs.repository;

import com.fusm.programs.entity.RenovationModuleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRenovationModuleStatusRepository extends JpaRepository<RenovationModuleStatus, Integer> {

    @Query(
            value = "SELECT * FROM renovation_module_status " +
                    "join proposal_renovation_modules " +
                    "on proposal_renovation_modules.id_renovation_module = renovation_module_status.proposal_renovation_module_id " +
                    "where status_id = 74 " +
                    "and proposal_renovation_modules.proposal_renovation_id = :renovationId",
            nativeQuery = true
    )
    List<RenovationModuleStatus> findAllModulesApproved(@Param("renovationId") Integer renovationId);

}
