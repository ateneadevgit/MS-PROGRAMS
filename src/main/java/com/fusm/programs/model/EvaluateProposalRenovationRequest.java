package com.fusm.programs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EvaluateProposalRenovationRequest {

    private String evaluation;
    private FileModel fileFeedback;
    private List<Integer> approvedModules;
    private String createdBy;
    private Integer roleId;

}
