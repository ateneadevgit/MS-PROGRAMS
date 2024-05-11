package com.fusm.programs.model.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectListModel {

    private Integer subjectId;
    private String name;
    private Integer semester;
    private Integer creditNumber;
    private Boolean isUpdated;
    private String createdBy;

}
