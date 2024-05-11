package com.fusm.programs.dto;

import org.springframework.beans.factory.annotation.Value;

public interface OwnProgramDto {

    @Value("#{target.id_program}")
    Integer getIdProgram();

    @Value("#{target.program_name}")
    String getName();

    @Value("#{target.duration}")
    String getDuration();

    @Value("#{target.schedule}")
    String getSchedule();

    @Value("#{target.credits}")
    Integer getCredits();

    @Value("#{target.modality_list}")
    String getModalityList();

    @Value("#{target.sinies}")
    String getSinies();

}
