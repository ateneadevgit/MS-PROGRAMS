package com.fusm.programs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramUpdateModel {

    private FileModel fileProposal;
    private String createdBy;
    private Integer roleId;

}
