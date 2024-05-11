package com.fusm.programs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurriculumRequest {

    private Integer curriculumId;
    private String name;
    private String code;
    private String raeg;
    private String description;
    private Integer hoursInteractionTeacher;
    private Integer hourSelfWork;
    private Integer type;
    private Integer creditNumber;
    private Double participation;
    private Integer fatherId;
    private Boolean isUpdated;
    private Integer semester;
    private Integer numberCredits;
    private Double percentageParticipation;
    private Long createdAt;

}
