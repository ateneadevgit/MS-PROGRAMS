package com.fusm.programs.service.impl;

import com.fusm.programs.entity.History;
import com.fusm.programs.entity.Program;
import com.fusm.programs.entity.ProgramModule;
import com.fusm.programs.entity.ProposalRenovation;
import com.fusm.programs.model.HistoryModel;
import com.fusm.programs.repository.IHistoryRepository;
import com.fusm.programs.repository.IProgramModuleRepository;
import com.fusm.programs.repository.IProgramRepository;
import com.fusm.programs.repository.IProposalRenovationRepository;
import com.fusm.programs.service.IHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HistoryService implements IHistoryService {

    @Autowired
    private IHistoryRepository historyRepository;

    @Autowired
    private IProgramModuleRepository programModuleRepository;

    @Autowired
    private IProgramRepository programRepository;

    @Autowired
    private IProposalRenovationRepository proposalRenovationRepository;


    @Override
    public List<HistoryModel> getHistorySpecific(Integer programId, Integer moduleId) {
        return historyRepository.findHistorySpecific(programId, moduleId).stream().map(
                history -> HistoryModel.builder()
                        .historyId(history.getHistoryId())
                        .version(history.getVersion())
                        .createdBy(history.getCreatedBy())
                        .editionDate(history.getUpdatedAt())
                        .value(history.getValue())
                        .minute(history.getMinute())
                        .build()
        ).toList();
    }

    @Override
    public List<HistoryModel> gitHistoricByType(Integer programId, Integer moduleId, Integer typeId) {
        return historyRepository.findHistoryByType(programId, moduleId, typeId).stream().map(
                history -> HistoryModel.builder()
                        .historyId(history.getHistoryId())
                        .version(history.getVersion())
                        .createdBy(history.getCreatedBy())
                        .editionDate(history.getUpdatedAt())
                        .value(history.getValue())
                        .minute(history.getMinute())
                        .build()
        ).toList();
    }

    @Override
    public void createHistoric(String value, Integer moduleId, Integer programId, String createdBy, Integer type) {
        Optional<ProgramModule> programModuleOptional = programModuleRepository.findById(moduleId);
        Optional<Program> programOptional = programRepository.findById(programId);

        if (programModuleOptional.isPresent() && programOptional.isPresent()) {
            List<History> lastVersion = (type != null) ? historyRepository.findLastVersionWithType(programId, moduleId, type) :
                    historyRepository.findLastVersion(programId, moduleId);
            double version = (!lastVersion.isEmpty()) ? lastVersion.get(0).getVersion() + 1 : 1;
            String minute = "";
            List<ProposalRenovation> proposalRenovationList = proposalRenovationRepository.findLastUpgradeRequest(programId);
            if (!proposalRenovationList.isEmpty()) minute = proposalRenovationList.get(0).getMinuteRenovation();
            historyRepository.save(
                    History.builder()
                            .value(value)
                            .version(version)
                            .createdAt(new Date())
                            .createdBy(createdBy)
                            .updatedAt(new Date())
                            .enabled(true)
                            .objectType(type)
                            .programId(programOptional.get())
                            .programModuleId(programModuleOptional.get())
                            .minute(minute)
                            .build()
            );
        }
    }

}
