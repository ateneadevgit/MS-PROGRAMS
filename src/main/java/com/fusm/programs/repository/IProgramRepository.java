package com.fusm.programs.repository;

import com.fusm.programs.dto.*;
import com.fusm.programs.entity.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProgramRepository extends JpaRepository<Program, Integer> {

    @Query(value = "SELECT * FROM get_programs(:status_type, :status_id, :faculty_id, :campus_id)",
            countQuery = "SELECT COUNT(*) FROM get_programs(:status_type, :status_id, :faculty_id, :campus_id)",
            nativeQuery = true)
    Page<ProgramDto> getProgram(@Param("status_type") Integer status_type,
                                @Param("status_id") Integer status_id,
                                @Param("faculty_id") Integer faculty_id,
                                @Param("campus_id") Integer campus_id,
                                Pageable pageable);

    @Query(value = "SELECT * FROM get_programs(:status_type, :status_id, :faculty_id, :campus_id)",
            nativeQuery = true)
    List<ProgramDto> getProgramList(@Param("status_type") Integer status_type,
                                @Param("status_id") Integer status_id,
                                @Param("faculty_id") Integer faculty_id,
                                @Param("campus_id") Integer campus_id);

    @Query(value = "SELECT * FROM get_tecnical_programs(:status_type, :status_id, :faculty_id, :campus_id)",
            nativeQuery = true)
    List<TecnicalProgramDto> getTecnicalProgramList(@Param("status_type") Integer status_type,
                                            @Param("status_id") Integer status_id,
                                            @Param("faculty_id") Integer faculty_id,
                                            @Param("campus_id") Integer campus_id);

    @Query(value = "SELECT * FROM get_program_by_id(:program_id)",
            nativeQuery = true)
    ProgramIdDto getProgramByIdNative(@Param("program_id") Integer programId);

    @Query(
            value = "select  " +
                    "status.role_id, " +
                    "status.id_status, " +
                    "status.created_at, " +
                    "status.feedback_file_url " +
                    "from status " +
                    "join programs on programs.id_program = status.program_id " +
                    "where programs.id_program = :id " +
                    "and status.status_type = :type " +
                    "order by status.created_at asc ",
            nativeQuery = true
    )
    List<HistoricDto> getProposalHistoric(
            @Param("id") Integer proposalId,
            @Param("type") Integer typeId
    );

    List<Program> findAllByTraceabilityId_TraceabilityId(Integer traceabilityId);

    @Query(
            value = "select * from get_our_programs(:levelFormation, :facultyId, :campusId, :modalityId)",
            nativeQuery = true
    )
    List<OwnProgramDto> findOwnPrograms(
            @Param("levelFormation") Integer levelFormation,
            @Param("facultyId") Integer facultyId,
            @Param("campusId") Integer campusId,
            @Param("modalityId") Integer modalityId
    );

}
