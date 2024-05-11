package com.fusm.programs.repository;

import com.fusm.programs.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface IStatusRepository extends JpaRepository<Status, Integer> {

    @Query(
            value = "SELECT * " +
                    "FROM status " +
                    "WHERE status.status_type = 19 " +
                    "and status.program_id = :id " +
                    "order by created_at desc ",
            nativeQuery = true
    )
    List<Status> finsLastUpgradeStatus(@Param("id") Integer programId);

    @Query(
            value = "SELECT created_at " +
                    "FROM public.status " +
                    "where program_id = :id " +
                    "and status_type = 22 " +
                    "order by created_at desc",
            nativeQuery = true
    )
    List<Date>  findApprovedProposalDate(@Param("id") Integer programId);

}
