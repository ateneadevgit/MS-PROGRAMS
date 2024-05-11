package com.fusm.programs.dto;

import com.fusm.programs.util.Constant;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;
import java.util.TimeZone;

public interface HistoricDto {

    TimeZone timeZoneColombia = TimeZone.getTimeZone("America/Bogota");

    @Value("#{target.role_id}")
    Integer getRolId();

    @Value("#{target.id_status}")
    Integer getStatus();

    @Value("#{target.feedback_file_url}")
    String getFileRoute();

    @Value("#{target.created_at}")
    Date getFeedbackDate();

}
