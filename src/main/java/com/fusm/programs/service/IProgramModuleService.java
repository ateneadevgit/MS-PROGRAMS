package com.fusm.programs.service;

import com.fusm.programs.model.ProgramModuleModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProgramModuleService {

    List<ProgramModuleModel> getProgramModules(Boolean isEnlarge);
    List<ProgramModuleModel> getAllProgramModules(Boolean isEnlarge);

}
