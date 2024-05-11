package com.fusm.programs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramModuleModel {

    private Integer moduleId;
    private String name;
    private Boolean isEditable;
    private Integer type;

}
