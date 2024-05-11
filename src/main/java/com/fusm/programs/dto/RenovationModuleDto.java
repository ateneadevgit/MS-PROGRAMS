package com.fusm.programs.dto;

import org.springframework.beans.factory.annotation.Value;

public interface RenovationModuleDto {

    @Value("#{target.module_id}")
    Integer getModuleId();

    @Value("#{target.name}")
    String getName();

    @Value("#{target.status}")
    Integer getStatus();

    @Value("#{target.module_type}")
    Integer getModuleType();

    @Value("#{target.has_evaluation}")
    Boolean getHasEvaluation();

}
