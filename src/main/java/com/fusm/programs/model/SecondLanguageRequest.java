package com.fusm.programs.model;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecondLanguageRequest {

    private FileModel cover;
    private String tittle;
    private String startLevel;
    private String endLevel;
    private Integer groupId;
    private FileModel icon;
    private String description;
    private Integer modalityId;
    private Integer duration;
    private Integer hours;
    private String inscriptionLink;
    private String createdBy;

}
