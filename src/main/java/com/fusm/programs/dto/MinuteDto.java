package com.fusm.programs.dto;

import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

public interface MinuteDto {

    @Value("#{target.id_program_attachment}")
    Integer getIdProgramAttachment();

    @Value("#{target.created_at}")
    Date getCreatedAt();

    @Value("#{target.created_by}")
    String getCreatedBy();

    @Value("#{target.updated_at}")
    Date getUpdatedAt();

    @Value("#{target.name}")
    String getName();

    @Value("#{target.file_url}")
    String getFileUrl();

    @Value("#{target.file_type}")
    Integer getFileType();

    @Value("#{target.attachment_father_id}")
    Integer getAttachmentFatherId();

}
