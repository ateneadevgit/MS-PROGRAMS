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
public class ProposalUpgradeRequest {

    private List<Integer> modulesId;
    private FileModel minute;
    private Boolean hasApproval;
    private String createdBy;
    private Integer roleId;

}
