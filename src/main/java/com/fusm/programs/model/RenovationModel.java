package com.fusm.programs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RenovationModel {

    private Integer facultyId;
    private List<Integer> campus;
    private Integer levelFormation;
    private Date requestDate;
    private String requestMinute;
    private String responseMinute;
    private List<ProgramModuleModel> selectedModules;

}
