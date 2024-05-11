package com.fusm.programs.service;

import com.fusm.programs.dto.RenovationModuleDto;
import com.fusm.programs.model.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProposalRenovationService {

    void createProposalRenovation(ProposalUpgradeRequest proposalUpgradeRequest, Integer programId);
    void evaluateProposalRenovation(EvaluateProposalRenovationRequest evaluateProposalRenovationRequest, Integer programId);
    RenovationModel getRequestProposal(Integer programId);
    List<ProgramModuleModel> getSelectedModules(Integer programId);
    OnEditionModules getOnEditionModules(Integer programId);

}
