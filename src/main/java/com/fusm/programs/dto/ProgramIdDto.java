package com.fusm.programs.dto;

import org.springframework.beans.factory.annotation.Value;

public interface ProgramIdDto {

    @Value("#{target.id_program}")
    Integer getIdProgram();

    @Value("#{target.program_name}")
    String getName();

    @Value("#{target.program_logo}")
    String getLogo();

    @Value("#{target.program_cover}")
    String getCover();

    @Value("#{target.development_date}")
    String getDevelopmentDate();

    @Value("#{target.id_faculty}")
    Integer getIdFaculty();

    @Value("#{target.id_type_formation}")
    Integer getIdTypeFormation();

    @Value("#{target.id_level_formation}")
    Integer getIdLevelFormation();

    @Value("#{target.id_registry_type}")
    Integer getIdRegistryType();

    @Value("#{target.file_url}")
    String getFileUrl();

    @Value("#{target.created_at}")
    String getCreatedAt();

    @Value("#{target.campus_list}")
    String getCampusList();

    @Value("#{target.modality_list}")
    String getModalityList();

}
