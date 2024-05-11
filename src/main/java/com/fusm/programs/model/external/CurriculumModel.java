package com.fusm.programs.model.external;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumModel {

    private Integer curriculumId;
    private String name;
    private Integer type;
    private String description;
    private Integer numberCredits;
    private Double percentageParticipation;
    private CoreModel coreModel;
    private SubjectModel subjectRequest;
    private Integer fatherId;
    private Long createdAt;
    private Boolean isUpdated;
    private List<CurriculumModel> childs;
    private String createdBy;

    public void addChild(CurriculumModel child) {
        childs.add(child);
    }

}
