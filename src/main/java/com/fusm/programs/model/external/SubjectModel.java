package com.fusm.programs.model.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectModel {

    private Integer semester;
    private String code;
    private Integer hoursInteractionTeacher;
    private Integer hourSelfWork;

}

