package com.fusm.programs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ObjectToReview {

    private Integer programId;
    private Integer moduleId;
    private String createdBy;
    private Integer roleId;

}
