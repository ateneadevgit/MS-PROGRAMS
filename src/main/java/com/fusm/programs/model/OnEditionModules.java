package com.fusm.programs.model;

import com.fusm.programs.dto.RenovationModuleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OnEditionModules {

    private Integer renovationId;
    private List<RenovationModuleDto> modules;

}
