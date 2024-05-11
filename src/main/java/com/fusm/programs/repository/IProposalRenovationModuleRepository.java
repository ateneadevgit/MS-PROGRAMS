package com.fusm.programs.repository;

import com.fusm.programs.dto.RenovationModuleDto;
import com.fusm.programs.entity.ProposalRenovationModules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IProposalRenovationModuleRepository extends JpaRepository<ProposalRenovationModules, Integer> {

    @Query(
            value = "SELECT * " +
                    "FROM proposal_renovation_modules " +
                    "join program_module " +
                    "on program_module.id_program_module = proposal_renovation_modules.program_module_id " +
                    "where proposal_renovation_modules.proposal_renovation_id = :proposalId " +
                    "order by program_module.module_order asc ",
            nativeQuery = true
    )
    List<ProposalRenovationModules> findAllByProposalRenovationByProgram(@Param("proposalId") Integer proposalRenovationId);

    @Query(
            value = "select * from get_modules_on_edition(:revonvationId)",
            nativeQuery = true
    )
    List<RenovationModuleDto> findAllModulesOnEdition(@Param("revonvationId") Integer proposalRenovationModuleId);

    List<ProposalRenovationModules> findAllByProposalRenovationId_ProposalRenovationIdAndProgramModuleId_ProgramModuleId(
            Integer proposalRenovationId, Integer programModuleId);

    @Query(
            value = "SELECT * " +
                    "FROM proposal_renovation_modules " +
                    "join program_module " +
                    "on program_module.id_program_module = proposal_renovation_modules.program_module_id " +
                    "where proposal_renovation_modules.proposal_renovation_id = :proposalId " +
                    "and proposal_renovation_modules.is_editable = true " +
                    "order by program_module.module_order asc ",
            nativeQuery = true
    )
    List<ProposalRenovationModules> findAllByProposalRenovationByProgramAndEditable(@Param("proposalId") Integer proposalRenovationId);

}
