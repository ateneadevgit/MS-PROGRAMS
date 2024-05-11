package com.fusm.programs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubjectRequest {

    private Integer subjectId;
    private Integer creditNumber;
    private String name;
    private Integer semester;
    private Boolean isUpdated;

}
