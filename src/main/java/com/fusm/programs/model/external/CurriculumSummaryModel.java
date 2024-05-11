package com.fusm.programs.model.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumSummaryModel {

    private Integer curriculumSummaryId;
    private String curriculumSummary;

}
