package com.fusm.programs.service.impl;

import com.fusm.programs.model.ProgramModuleModel;
import com.fusm.programs.repository.IProgramModuleRepository;
import com.fusm.programs.service.IProgramModuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramModuleService implements IProgramModuleService {

    @Autowired
    private IProgramModuleRepository programModuleRepository;


    @Override
    public List<ProgramModuleModel> getProgramModules(Boolean isEnlarge) {
        return programModuleRepository.findAllByModules(isEnlarge).stream().map(
                module -> ProgramModuleModel.builder()
                        .moduleId(module.getProgramModuleId())
                        .name(module.getName())
                        .type(module.getModuleType())
                        .build()
        ).toList();
    }

    @Override
    public List<ProgramModuleModel> getAllProgramModules(Boolean isEnlarge) {
        return programModuleRepository.findAllByModulesNoEditables(isEnlarge).stream().map(
                module -> ProgramModuleModel.builder()
                        .moduleId(module.getProgramModuleId())
                        .name(module.getName())
                        .type(module.getModuleType())
                        .build()
        ).toList();
    }

}
