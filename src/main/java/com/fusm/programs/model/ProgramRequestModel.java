package com.fusm.programs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramRequestModel {

    private FileModel logo;
    private FileModel cover;
    private String programName;
    private Integer facultyId;
    private Integer formationTypeId;
    private Integer[] campus;
    private Integer formationLevel;
    private Integer[] modality;
    private String developmentDate;
    private FileModel fileProposal;
    private Boolean isEnlarge;
    private Integer programFather;
    private Integer registryTypeId;
    private String createdBy;
    private Integer roleId;

}
