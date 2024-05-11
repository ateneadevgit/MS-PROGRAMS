package com.fusm.programs.dto;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public interface TecnicalProgramDto {

    @Value("#{target.id_program}")
    Integer getIdProgram();

    @Value("#{target.program_name}")
    String getName();

    @Value("#{target.campus_list}")
    String getCampusList();

    @Value("#{target.id_level_formation}")
    Integer getIdLevelFormation();

    @Value("#{target.men_end_date}")
    Date getMenEndDate();

}
