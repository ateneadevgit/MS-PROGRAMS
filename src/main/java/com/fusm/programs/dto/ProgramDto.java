package com.fusm.programs.dto;

import org.springframework.beans.factory.annotation.Value;

public interface ProgramDto {

    @Value("#{target.id_program}")
    Integer getIdProgram();

    @Value("#{target.program_name}")
    String getName();

    @Value("#{target.faculty_id}")
    Integer getFacultyId();

    @Value("#{target.id_type_formation}")
    Integer getIdTypeFormation();

}
