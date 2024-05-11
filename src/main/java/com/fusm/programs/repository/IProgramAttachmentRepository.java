package com.fusm.programs.repository;

import com.fusm.programs.dto.MinuteDto;
import com.fusm.programs.entity.ProgramAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;
import java.util.List;

@Repository
public interface IProgramAttachmentRepository extends JpaRepository<ProgramAttachment, Integer> {

    @Query(
            value = "select * from program_attachment " +
                    "where file_type = 85 " +
                    "and enabled = true " +
                    "order by order_file asc ",
            nativeQuery = true
    )
    List<ProgramAttachment> findAllOrdered();

    @Query(
            value = "select * from program_attachment " +
                    "where file_type = :type " +
                    "order by id_program_attachment desc ",
            nativeQuery = true
    )
    List<ProgramAttachment> findAllOrderedByType(
            @Param("type") Integer type
    );

    @Query(
            value = "select * from get_minute_attachment(:date, :name)",
            nativeQuery = true
    )
    List<MinuteDto> findAllMinutes(
            @Param("date") Date filedate,
            @Param("name") String fileName
            );

}
