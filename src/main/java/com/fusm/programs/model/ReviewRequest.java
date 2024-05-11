package com.fusm.programs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequest {

    private String review;
    private String createdBy;
    private Integer roleId;
    private Integer replyTo;
    private Integer objectId;
    private Integer objectComplementaryId;
    private Integer objectType;

}
