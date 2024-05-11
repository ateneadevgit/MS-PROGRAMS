package com.fusm.programs.repository;

import com.fusm.programs.entity.ObjectTemp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IObjectTempRepository extends JpaRepository<ObjectTemp, Integer> {

    @Query(
            value = "select * from object_temp " +
                    "where proposal_renovation_module_id = :renovationId " +
                    "order by created_at desc ",
            nativeQuery = true
    )
    List<ObjectTemp> getLastObject(@Param("renovationId") Integer renovationId);

    List<ObjectTemp> findAllByProposalRenovationModulesId_RenovationModuleId(Integer proposalRenovationModuleId);

}
