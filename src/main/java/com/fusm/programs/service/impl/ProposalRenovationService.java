package com.fusm.programs.service.impl;

import com.fusm.programs.dto.RenovationModuleDto;
import com.fusm.programs.entity.*;
import com.fusm.programs.external.IWorkflowService;
import com.fusm.programs.model.*;
import com.fusm.programs.model.external.CoreAndSubcoreModel;
import com.fusm.programs.model.external.CurriculumModel;
import com.fusm.programs.model.external.SubjectListModel;
import com.fusm.programs.repository.*;
import com.fusm.programs.service.ICampusService;
import com.fusm.programs.service.IHistoryService;
import com.fusm.programs.service.IProposalRenovationService;
import com.fusm.programs.util.Constant;
import com.fusm.programs.util.SharedMethod;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ProposalRenovationService implements IProposalRenovationService {

    @Autowired
    private IProposalRenovationRepository proposalRenovationRepository;

    @Autowired
    private IProgramRepository programRepository;

    @Autowired
    private IProgramModuleRepository programModuleRepository;

    @Autowired
    private IProposalRenovationModuleRepository proposalRenovationModuleRepository;

    @Autowired
    private IStatusRepository statusRepository;

    @Autowired
    private SharedMethod sharedMethod;

    @Autowired
    private IRenovationModuleStatusRepository renovationModuleStatusRepository;

    @Autowired
    private IObjectTempRepository objectTempRepository;

    @Autowired
    private IWorkflowService workflowService;

    @Autowired
    private IHistoryService historyService;

    @Autowired
    private ICampusService campusService;

    Gson gson = new Gson();


    @Override
    public void createProposalRenovation(ProposalUpgradeRequest proposalUpgradeRequest, Integer programId) {
        Optional<Program> programOptional = programRepository.findById(programId);

        if (programOptional.isPresent()) {
            ProposalRenovation proposalRenovation = proposalRenovationRepository.save(
                    ProposalRenovation.builder()
                            .hasEvaluation(proposalUpgradeRequest.getHasApproval())
                            .minuteRenovation(sharedMethod.saveFile(proposalUpgradeRequest.getMinute(), proposalUpgradeRequest.getCreatedBy()))
                            .status((proposalUpgradeRequest.getHasApproval()) ? Constant.PROPOSAL_UPGRADE_FOR_EVALUATION
                                    : Constant.PROPOSAL_UPGRADE_EVALUATED)
                            .createdBy(proposalUpgradeRequest.getCreatedBy())
                            .createdAt(new Date())
                            .programId(programOptional.get())
                            .build()
            );

            Integer curriculumModule = sharedMethod.getSettingValue(Constant.MODULE_CURRICULUM_TYPE);
            Integer creditNumberModule = sharedMethod.getSettingValue(Constant.MODULE_ACADEMIC_CREDIT_TYPE);
            Integer subCoreModule = sharedMethod.getSettingValue(Constant.MODULE_SUBCORE_TYPE);
            Integer coreAndSubcoreModule = sharedMethod.getSettingValue(Constant.MODULE_CORE_AND_SUBCORE_TYPE);

            for (Integer moduleId : proposalUpgradeRequest.getModulesId()) {
                Optional<ProgramModule> moduleOptional = programModuleRepository.findById(moduleId);
                if (moduleOptional.isPresent()) {
                    ProgramModule programModule = moduleOptional.get();
                    ProposalRenovationModules proposalRenovationModules = proposalRenovationModuleRepository.save(
                            ProposalRenovationModules.builder()
                                    .proposalRenovationId(proposalRenovation)
                                    .programModuleId(programModule)
                                    .isEditable(!proposalUpgradeRequest.getHasApproval())
                                    .build());

                    if (!proposalUpgradeRequest.getHasApproval()) {
                        createDefaultStatus(proposalRenovationModules, proposalUpgradeRequest.getCreatedBy(), Constant.MODULE_ON_PROJECTION_STATUS);
                        if (programModule.getModuleType().equals(curriculumModule)) createObjectTempCurriculum(programId, proposalRenovationModules, proposalUpgradeRequest.getCreatedBy());
                        if (programModule.getModuleType().equals(creditNumberModule)) createObjectTempSubjectCredits(programId, proposalRenovationModules, proposalUpgradeRequest.getCreatedBy());
                        if (programModule.getModuleType().equals(subCoreModule) || (programModule.getModuleType().equals(coreAndSubcoreModule)))
                            createObjectTempCurricularCore(programId, proposalRenovationModules, proposalUpgradeRequest.getCreatedBy());
                    }
                }
            }

            String status = (proposalUpgradeRequest.getHasApproval()) ? Constant.UPGRADE_REQUEST_STATUS : Constant.UPGRADE_ON_UPDATE_STATUS;
            saveStatus(proposalUpgradeRequest.getCreatedBy(), proposalUpgradeRequest.getRoleId(), programOptional.get(),
                    status, Constant.STATUS_UPGRADE);
        }

    }

    @Override
    public void evaluateProposalRenovation(EvaluateProposalRenovationRequest evaluateProposalRenovationRequest, Integer programId) {
        List<ProposalRenovation> proposalRenovationList = proposalRenovationRepository.findLastUpgradeRequest(programId);
        Optional<Program> programOptional = programRepository.findById(programId);

        if (!proposalRenovationList.isEmpty() && programOptional.isPresent()) {
            ProposalRenovation proposalRenovation = proposalRenovationList.get(0);

            Integer curriculumModule = sharedMethod.getSettingValue(Constant.MODULE_CURRICULUM_TYPE);
            Integer creditNumberModule = sharedMethod.getSettingValue(Constant.MODULE_ACADEMIC_CREDIT_TYPE);
            Integer subCoreModule = sharedMethod.getSettingValue(Constant.MODULE_SUBCORE_TYPE);
            Integer coreAndSubcoreModule = sharedMethod.getSettingValue(Constant.MODULE_CORE_AND_SUBCORE_TYPE);

            List<ProposalRenovationModules> proposalRenovationModules = proposalRenovationModuleRepository
                    .findAllByProposalRenovationByProgram(proposalRenovation.getProposalRenovationId());

            if (evaluateProposalRenovationRequest.getEvaluation().equals("approved")) {
                if (proposalRenovation.getHasEvaluation()) {
                    historyService.createHistoric(
                            gson.toJson(proposalRenovation),
                            Constant.MODULE_UPDATE_AUTHORIZATIONS,
                            programId,
                            proposalRenovation.getCreatedBy(),
                            null
                    );
                }
                for (ProposalRenovationModules proposal : proposalRenovationModules) {
                    if (evaluateProposalRenovationRequest.getApprovedModules().contains(proposal.getProgramModuleId().getProgramModuleId())) {
                        proposal.setIsEditable(true);
                        proposalRenovationModuleRepository.save(proposal);
                        createDefaultStatus(proposal, evaluateProposalRenovationRequest.getCreatedBy(), Constant.MODULE_ON_PROJECTION_STATUS);
                        if (proposal.getProgramModuleId().getModuleType().equals(curriculumModule))
                            createObjectTempCurriculum(programId, proposal, evaluateProposalRenovationRequest.getCreatedBy());
                        if (proposal.getProgramModuleId().getModuleType().equals(creditNumberModule))
                            createObjectTempSubjectCredits(programId, proposal, evaluateProposalRenovationRequest.getCreatedBy());
                        if (proposal.getProgramModuleId().getModuleType().equals(subCoreModule) || (proposal.getProgramModuleId().getModuleType().equals(coreAndSubcoreModule)))
                            createObjectTempCurricularCore(programId, proposal, evaluateProposalRenovationRequest.getCreatedBy());
                    }
                }
                saveStatus(evaluateProposalRenovationRequest.getCreatedBy(), evaluateProposalRenovationRequest.getRoleId(), programOptional.get(),
                        Constant.UPGRADE_ON_UPDATE_STATUS, Constant.STATUS_UPGRADE);
            } else if (evaluateProposalRenovationRequest.getEvaluation().equals("declined")) {
                saveStatus(evaluateProposalRenovationRequest.getCreatedBy(), evaluateProposalRenovationRequest.getRoleId(), programOptional.get(),
                        Constant.UPGRADE_DECLINED_STATUS, Constant.STATUS_UPGRADE);
            }
            proposalRenovation.setStatus(Constant.PROPOSAL_UPGRADE_EVALUATED);

        }
    }

    @Override
    public RenovationModel getRequestProposal(Integer programId) {
        List<ProposalRenovation> proposalRenovationList = proposalRenovationRepository.findLastUpgradeRequest(programId);
        Optional<Program> programOptional = programRepository.findById(programId);
        RenovationModel renovationModel = new RenovationModel();

        if (!proposalRenovationList.isEmpty() && programOptional.isPresent()) {
            ProposalRenovation proposalRenovation = proposalRenovationList.get(0);
            renovationModel = RenovationModel.builder()
                    .facultyId(programOptional.get().getFacultyId())
                    .campus(campusService.getCampusByProgram(programId))
                    .levelFormation(programOptional.get().getLevelFormationId())
                    .requestDate(proposalRenovation.getCreatedAt())
                    .requestMinute(proposalRenovation.getMinuteRenovation())
                    .responseMinute(proposalRenovation.getMinuteResponse())
                    .selectedModules(getSelectedModules(programId))
                    .build();
        }

        return renovationModel;
    }

    @Override
    public List<ProgramModuleModel> getSelectedModules(Integer programId) {
        List<ProposalRenovation> proposalRenovationList = proposalRenovationRepository.findLastUpgradeRequest(programId);
        Optional<Program> programOptional = programRepository.findById(programId);
        List<ProgramModuleModel> selectedModules = new ArrayList<>();

        if (!proposalRenovationList.isEmpty() && programOptional.isPresent()) {
            ProposalRenovation proposalRenovation = proposalRenovationList.get(0);
            selectedModules = proposalRenovationModuleRepository
                    .findAllByProposalRenovationByProgram(proposalRenovation.getProposalRenovationId()).stream().map(
                            module -> ProgramModuleModel.builder()
                                    .moduleId(module.getProgramModuleId().getProgramModuleId())
                                    .name(module.getProgramModuleId().getName())
                                    .isEditable(module.getIsEditable())
                                    .build()
                    ).toList();
        }
        return selectedModules;
    }

    @Override
    public OnEditionModules getOnEditionModules(Integer programId) {
        List<ProposalRenovation> proposalRenovationList = proposalRenovationRepository.findLastUpgradeRequest(programId);
        Optional<Program> programOptional = programRepository.findById(programId);
        List<RenovationModuleDto> selectedModules = new ArrayList<>();
        OnEditionModules onEditionModules = new OnEditionModules();

        if (!proposalRenovationList.isEmpty() && programOptional.isPresent()) {
            ProposalRenovation proposalRenovation = proposalRenovationList.get(0);
            selectedModules = proposalRenovationModuleRepository
                    .findAllModulesOnEdition(proposalRenovation.getProposalRenovationId());
            onEditionModules.setRenovationId(proposalRenovation.getProposalRenovationId());
            onEditionModules.setModules(selectedModules);
        }
        return onEditionModules;
    }

    private void saveStatus(String createdBy, Integer roleId, Program program, String status, String type) {
        statusRepository.save(
                Status.builder()
                        .statusId(sharedMethod.getSettingValue(status))
                        .statusType(sharedMethod.getSettingValue(type))
                        .createdAt(new Date(System.currentTimeMillis() - 10 * 60 * 60 * 1000))
                        .createdBy(createdBy)
                        .programId(program)
                        .roleId(roleId)
                        .build()
        );
    }

    private void createDefaultStatus(ProposalRenovationModules renovation, String createdBy, String status) {
        renovationModuleStatusRepository.save(
                RenovationModuleStatus.builder()
                        .statusId(sharedMethod.getSettingValue(status))
                        .createdBy(createdBy)
                        .createdAt(new Date())
                        .proposalRenovationModulesId(renovation)
                        .build()
        );
    }

    private void createObjectTempCurriculum(Integer programId, ProposalRenovationModules proposalRenovationModules, String createdBy) {
        List<CurriculumModel> curriculumModelList = workflowService.getCurriculumWithoutCore(programId);
        objectTempRepository.save(
                ObjectTemp.builder()
                        .objectValue(gson.toJson(curriculumModelList))
                        .proposalRenovationModulesId(proposalRenovationModules)
                        .createdBy(createdBy)
                        .createdAt(new Date())
                        .build()
        );
    }

    private void createObjectTempSubjectCredits(Integer programId, ProposalRenovationModules proposalRenovationModules, String createdBy) {
        List<SubjectListModel> subjectListModels = workflowService.getSubjects(programId);
        objectTempRepository.save(
                ObjectTemp.builder()
                        .objectValue(gson.toJson(subjectListModels))
                        .proposalRenovationModulesId(proposalRenovationModules)
                        .createdBy(createdBy)
                        .createdAt(new Date())
                        .build()
        );
    }

    private void createObjectTempCurricularCore(Integer programId, ProposalRenovationModules proposalRenovationModules, String createdBy) {
        List<CoreAndSubcoreModel> coreAndSubcoreModels = workflowService.getCoreAndSubcore(programId);
        objectTempRepository.save(
                ObjectTemp.builder()
                        .objectValue(gson.toJson(coreAndSubcoreModels))
                        .proposalRenovationModulesId(proposalRenovationModules)
                        .createdBy(createdBy)
                        .createdAt(new Date())
                        .build()
        );
    }

}
