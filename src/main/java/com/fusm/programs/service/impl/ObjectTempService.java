package com.fusm.programs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fusm.programs.entity.*;
import com.fusm.programs.external.IWorkflowService;
import com.fusm.programs.model.*;
import com.fusm.programs.model.external.CoreAndSubcoreModel;
import com.fusm.programs.model.external.CurriculumModel;
import com.fusm.programs.model.external.SubjectListModel;
import com.fusm.programs.repository.*;
import com.fusm.programs.service.IObjectTempService;
import com.fusm.programs.service.specific.CurriculumMapper;
import com.fusm.programs.service.specific.FlatterCurriculum;
import com.fusm.programs.util.Constant;
import com.fusm.programs.util.SharedMethod;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.SchemaOutputResolver;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ObjectTempService implements IObjectTempService {

    @Autowired
    private IObjectTempRepository objectTempRepository;

    @Autowired
    private IProposalRenovationRepository proposalRenovationRepository;

    @Autowired
    private IProgramRepository programRepository;

    @Autowired
    private IProposalRenovationModuleRepository proposalRenovationModuleRepository;

    @Autowired
    private IProgramModuleRepository programModuleRepository;

    @Autowired
    private ICurriculumSummaryRepository curriculumSummaryRepository;

    @Autowired
    private IWorkflowService workflowService;

    @Autowired
    private IRenovationModuleStatusRepository renovationModuleStatusRepository;

    @Autowired
    private IStatusRepository statusRepository;

    @Autowired
    private ISyllabusRepository syllabusRepository;

    @Autowired
    private IWorkflowBaseStepRepository workflowBaseStepRepository;

    @Autowired
    private SharedMethod sharedMethod;

    ObjectMapper objectMapper = new ObjectMapper();

    FlatterCurriculum flatterCurriculum = new FlatterCurriculum();

    CurriculumMapper curriculumMapper = new CurriculumMapper();

    Gson gson = new Gson();


    @Override
    public void createdObjectTemp(ObjectTempRequest objectTempRequest, Integer programId) throws JsonProcessingException {
        List<ProposalRenovation> proposalRenovationList = proposalRenovationRepository.findLastUpgradeRequest(programId);
        Optional<Program> programOptional = programRepository.findById(programId);

        if (!proposalRenovationList.isEmpty() && programOptional.isPresent()) {
            ProposalRenovation proposalRenovation = proposalRenovationList.get(0);

            Integer curriculumModule = sharedMethod.getSettingValue(Constant.MODULE_CURRICULUM_TYPE);
            Integer creditNumberModule = sharedMethod.getSettingValue(Constant.MODULE_ACADEMIC_CREDIT_TYPE);
            Integer syllabusModule = sharedMethod.getSettingValue(Constant.MODULE_SYLLABUS_TYPE);
            Integer subCoreModule = sharedMethod.getSettingValue(Constant.MODULE_SUBCORE_TYPE);
            Integer coreAndCoreModule = sharedMethod.getSettingValue(Constant.MODULE_CORE_AND_SUBCORE_TYPE);

            List<ProposalRenovationModules> proposalRenovationModules = proposalRenovationModuleRepository
                    .findAllByProposalRenovationId_ProposalRenovationIdAndProgramModuleId_ProgramModuleId(
                            proposalRenovation.getProposalRenovationId(), objectTempRequest.getModuleId());

            if (!proposalRenovationModules.isEmpty()) {
                if (proposalRenovationModules.get(0).getProgramModuleId().getModuleType().equals(curriculumModule) ||
                        proposalRenovationModules.get(0).getProgramModuleId().getModuleType().equals(creditNumberModule) ||
                        proposalRenovationModules.get(0).getProgramModuleId().getModuleType().equals(syllabusModule) ||
                        proposalRenovationModules.get(0).getProgramModuleId().getModuleType().equals(subCoreModule) ||
                        proposalRenovationModules.get(0).getProgramModuleId().getModuleType().equals(coreAndCoreModule)) {
                    updateObjectTemp(objectTempRequest, programId);
                } else {
                    objectTempRepository.save(
                            ObjectTemp.builder()
                                    .objectValue(objectTempRequest.getObject())
                                    .proposalRenovationModulesId(proposalRenovationModules.get(0))
                                    .createdBy(objectTempRequest.getCreatedBy())
                                    .createdAt(new Date())
                                    .build()
                    );
                }
            }
            /*if (proposalRenovation.getHasEvaluation()) {
                saveRenovationStatus(proposalRenovationModules.get(0), objectTempRequest.getCreatedBy(), Constant.MODULE_ON_REVITION_STATUS);
            }*/
        }
    }

    @Override
    public void evaluateObject(EvaluateObjectRequest evaluateObjectRequest, Integer programId) throws JsonProcessingException {
        List<ProposalRenovation> proposalRenovationList = proposalRenovationRepository.findLastUpgradeRequest(programId);
        Optional<Program> programOptional = programRepository.findById(programId);

        if (!proposalRenovationList.isEmpty() && programOptional.isPresent()) {
            ProposalRenovation proposalRenovation = proposalRenovationList.get(0);

            List<ProposalRenovationModules> proposalRenovationModules = proposalRenovationModuleRepository
                    .findAllByProposalRenovationId_ProposalRenovationIdAndProgramModuleId_ProgramModuleId(
                            proposalRenovation.getProposalRenovationId(), evaluateObjectRequest.getModuleId());

            if (!proposalRenovationModules.isEmpty()) {
                String status = Constant.MODULE_ON_PROJECTION_STATUS;

                if (evaluateObjectRequest.getEvaluation().equals("approved")) {
                    status = Constant.MODULE_APPROVED_STATUS;
                    getObjectCreated(evaluateObjectRequest, proposalRenovationModules.get(0).getRenovationModuleId(),
                            evaluateObjectRequest.getModuleId(), programId);
                }

                if (evaluateObjectRequest.getEvaluation().equals("declined")) status = Constant.MODULE_ON_UPDATE_STATUS;
                saveRenovationStatus(proposalRenovationModules.get(0), evaluateObjectRequest.getCreatedBy(), status);

                if (validateAllModulesApproved(proposalRenovation.getProposalRenovationId())) {
                     saveStatus(evaluateObjectRequest.getCreatedBy(), evaluateObjectRequest.getRoleId(),
                             programOptional.get(), Constant.UPGRADE_APPROVED_STATUS, Constant.STATUS_UPGRADE);
                }
            }
        }
    }

    @Override
    public void sendObjectToReview(ObjectToReview objectToReview) throws JsonProcessingException {
        List<ProposalRenovation> proposalRenovationList = proposalRenovationRepository.findLastUpgradeRequest(objectToReview.getProgramId());
        Optional<Program> programOptional = programRepository.findById(objectToReview.getProgramId());

        if (!proposalRenovationList.isEmpty() && programOptional.isPresent()) {
            ProposalRenovation proposalRenovation = proposalRenovationList.get(0);

            List<ProposalRenovationModules> proposalRenovationModules = proposalRenovationModuleRepository
                    .findAllByProposalRenovationId_ProposalRenovationIdAndProgramModuleId_ProgramModuleId(
                            proposalRenovation.getProposalRenovationId(), objectToReview.getModuleId());
            saveRenovationStatus(proposalRenovationModules.get(0), objectToReview.getCreatedBy(), Constant.MODULE_ON_REVITION_STATUS);
            if (!proposalRenovation.getHasEvaluation()) {
                evaluateObject(
                        EvaluateObjectRequest.builder()
                                .evaluation("approved")
                                .moduleId(objectToReview.getModuleId())
                                .createdBy(objectToReview.getCreatedBy())
                                .roleId(objectToReview.getRoleId())
                            .build(),
                        objectToReview.getProgramId());
                //saveRenovationStatus(proposalRenovationModules.get(0), objectToReview.getCreatedBy(), Constant.MODULE_APPROVED_STATUS);
            }
        }
    }

    @Override
    public ObjectTempModel getObjectTemp(Integer programId, Integer moduleId) {
        List<ProposalRenovation> proposalRenovationList = proposalRenovationRepository.findLastUpgradeRequest(programId);
        Optional<Program> programOptional = programRepository.findById(programId);

        ObjectTempModel objectTempModel = new ObjectTempModel();

        if (!proposalRenovationList.isEmpty() && programOptional.isPresent()) {
            ProposalRenovation proposalRenovation = proposalRenovationList.get(0);

            List<ProposalRenovationModules> proposalRenovationModules = proposalRenovationModuleRepository
                    .findAllByProposalRenovationId_ProposalRenovationIdAndProgramModuleId_ProgramModuleId(
                            proposalRenovation.getProposalRenovationId(), moduleId);

            if (!proposalRenovationModules.isEmpty()) {
               List<ObjectTemp> objectTempList = objectTempRepository
               .getLastObject(proposalRenovationModules.get(0).getRenovationModuleId());

               if (!objectTempList.isEmpty()) {
                   objectTempModel = ObjectTempModel.builder()
                           .objectTempId(objectTempList.get(0).getObjectTempId())
                           .value(objectTempList.get(0).getObjectValue())
                           .build();
               }
            }
        }

        return objectTempModel;
    }

    @Override
    public void updateObjectTemp(ObjectTempRequest objectTempRequest, Integer programId) throws JsonProcessingException {
        List<ProposalRenovation> proposalRenovationList = proposalRenovationRepository.findLastUpgradeRequest(programId);
        if (!proposalRenovationList.isEmpty()) {
            ProposalRenovation proposalRenovation = proposalRenovationList.get(0);
            List<ProposalRenovationModules> proposalRenovationModulesList = proposalRenovationModuleRepository
                    .findAllByProposalRenovationId_ProposalRenovationIdAndProgramModuleId_ProgramModuleId(
                            proposalRenovation.getProposalRenovationId(), objectTempRequest.getModuleId());
            if (!proposalRenovationModulesList.isEmpty()) {
                List<ObjectTemp> objectTempOptional = objectTempRepository
                        .findAllByProposalRenovationModulesId_RenovationModuleId(proposalRenovationModulesList.get(0).getRenovationModuleId());

                Integer syllabusModule = sharedMethod.getSettingValue(Constant.MODULE_SYLLABUS_TYPE);
                Integer subCoreModule = sharedMethod.getSettingValue(Constant.MODULE_SUBCORE_TYPE);
                Integer coreAndSubcoreModule = sharedMethod.getSettingValue(Constant.MODULE_CORE_AND_SUBCORE_TYPE);
                Integer curricularComponentModule = sharedMethod.getSettingValue(Constant.MODULE_CURRICULAR_COMPONENT_TYPE);

                if (!objectTempOptional.isEmpty()) {
                    ObjectTemp objectTemp = objectTempOptional.get(0);
                    objectTemp.setCreatedBy(objectTempRequest.getCreatedBy());
                    Integer curriculumModule = sharedMethod.getSettingValue(Constant.MODULE_CURRICULUM_TYPE);
                    Integer creditNumberModule = sharedMethod.getSettingValue(Constant.MODULE_ACADEMIC_CREDIT_TYPE);

                    if (proposalRenovationModulesList.get(0).getProgramModuleId().getModuleType().equals(curriculumModule)) {
                        updateCurriculum(objectTemp, objectTempRequest);
                    } else if (proposalRenovationModulesList.get(0).getProgramModuleId().getModuleType().equals(creditNumberModule)) {
                        updateSubjectCredits(objectTemp, objectTempRequest);
                    } else if (proposalRenovationModulesList.get(0).getProgramModuleId().getModuleType().equals(syllabusModule)) {
                        updateSyllabus(objectTemp, objectTempRequest);
                    } else if (proposalRenovationModulesList.get(0).getProgramModuleId().getModuleType().equals(subCoreModule) ||
                            proposalRenovationModulesList.get(0).getProgramModuleId().getModuleType().equals(coreAndSubcoreModule)) {
                        updateCurricularSubcore(objectTemp, objectTempRequest);
                    } else if (proposalRenovationModulesList.get(0).getProgramModuleId().getModuleType().equals(curricularComponentModule)) {
                        updateCurricularComponent(objectTemp, objectTempRequest);
                    } else {
                        objectTemp.setObjectValue(objectTempRequest.getObject());
                        objectTempRepository.save(objectTemp);
                    }
                } else if (proposalRenovationModulesList.get(0).getProgramModuleId().getModuleType().equals(syllabusModule) ||
                        proposalRenovationModulesList.get(0).getProgramModuleId().getModuleType().equals(curricularComponentModule) ) {
                    objectTempRepository.save(
                            ObjectTemp.builder()
                                    .objectValue(objectTempRequest.getObject())
                                    .proposalRenovationModulesId(proposalRenovationModulesList.get(0))
                                    .createdBy(objectTempRequest.getCreatedBy())
                                    .createdAt(new Date(System.currentTimeMillis()))
                                    .build()
                    );
                }
            }

        }
    }

    @Override
    public int getObjectNumberCredits(Integer moduleId, Integer programId) throws JsonProcessingException {
        int credits = 0;

        Optional<ProgramModule> programModuleOptional = programModuleRepository.findById(moduleId);

        if (programModuleOptional.isPresent()) {
            if (programModuleOptional.get().getModuleType().equals(sharedMethod.getSettingValue(Constant.MODULE_ACADEMIC_CREDIT_TYPE))) {
                ObjectTempModel objectTempModel = getObjectTemp(programId, moduleId);
                List<SubjectListModel> subjectModelList = objectMapper
                        .readValue(objectTempModel.getValue(), new TypeReference<List<SubjectListModel>>() {});
                for (SubjectListModel subjectListModel : subjectModelList) credits += subjectListModel.getCreditNumber();
            }
        }
        return credits;
    }

    @Override
    public List<CoreAndSubcoreModel> getCores(
            Integer moduleId, Integer programId) throws JsonProcessingException {
        return searchCoreOrSubcore(null, moduleId, programId);
    }

    @Override
    public List<CoreAndSubcoreModel> getSubjectsByCore(
            Integer fatherId, Integer moduleId, Integer programId) throws JsonProcessingException {
        return searchCoreOrSubcore(fatherId, moduleId, programId);
    }

    private List<CoreAndSubcoreModel> searchCoreOrSubcore(Integer fatherId, Integer moduleId, Integer programId) throws JsonProcessingException {
        List<CoreAndSubcoreModel> coreAndSubcoreModels = new ArrayList<>();
        List<ProposalRenovation> proposalRenovationList = proposalRenovationRepository.findLastUpgradeRequest(programId);

        if (!proposalRenovationList.isEmpty()) {
            ProposalRenovation proposalRenovation = proposalRenovationList.get(0);
            List<ProposalRenovationModules> proposalRenovationModulesList = proposalRenovationModuleRepository
                    .findAllByProposalRenovationId_ProposalRenovationIdAndProgramModuleId_ProgramModuleId(
                            proposalRenovation.getProposalRenovationId(), moduleId);
            if (!proposalRenovationModulesList.isEmpty()) {
                List<ObjectTemp> objectTempOptional = objectTempRepository
                        .findAllByProposalRenovationModulesId_RenovationModuleId(proposalRenovationModulesList.get(0).getRenovationModuleId());

                if (!objectTempOptional.isEmpty()) {
                    ObjectTemp objectTemp = objectTempOptional.get(0);
                    List<CoreAndSubcoreModel> curriculumModelList = objectMapper
                            .readValue(objectTemp.getObjectValue(), new TypeReference<List<CoreAndSubcoreModel>>() {});
                    if (fatherId != null) {
                        coreAndSubcoreModels = curriculumModelList.stream()
                                .filter(coreAndSubcoreModel -> coreAndSubcoreModel.getFatherId().equals(fatherId))
                                .collect(Collectors.toList());
                    } else {
                        coreAndSubcoreModels = curriculumModelList.stream()
                                .filter(coreAndSubcoreModel -> coreAndSubcoreModel.getType().equals(53))
                                .collect(Collectors.toList());
                    }
                }
            }
        }
        return coreAndSubcoreModels;
    }

    private void getObjectCreated(EvaluateObjectRequest evaluateObjectRequest, Integer renovationId, Integer moduleId, Integer programId) throws JsonProcessingException {
        List<ObjectTemp> objectTempList = objectTempRepository.getLastObject(renovationId);
        if (!objectTempList.isEmpty()) {
            ObjectTempRequest objectTempRequest = ObjectTempRequest.builder()
                    .object(objectTempList.get(0).getObjectValue())
                    .moduleId(moduleId)
                    .createdBy(objectTempList.get(0).getCreatedBy())
                    .roleId(evaluateObjectRequest.getRoleId())
                            .build();
            createRealObject(objectTempRequest, programId);
        }
    }

    private void createRealObject(ObjectTempRequest objectTempRequest, Integer programId) throws JsonProcessingException {
        Optional<ProgramModule> programModule = programModuleRepository.findById(objectTempRequest.getModuleId());

        if (programModule.isPresent()) {
            ProgramModule module = programModule.get();
           if (module.getModuleType().equals(sharedMethod.getSettingValue(Constant.MODULE_SPECIFIC_SUMMARY_TYPE))) {
                createCurriculumSummary(objectTempRequest, programId);
            }

            if (module.getModuleType().equals(sharedMethod.getSettingValue(Constant.MODULE_SYLLABUS_TYPE))) {
                createSyllabus(objectTempRequest, programId);
            }

            if (module.getModuleType().equals(sharedMethod.getSettingValue(Constant.MODULE_ACADEMIC_CREDIT_TYPE))) {
                createAcademicCredit(objectTempRequest, programId);
            }

            if (module.getModuleType().equals(sharedMethod.getSettingValue(Constant.MODULE_SUBCORE_TYPE))
                || module.getModuleType().equals(sharedMethod.getSettingValue(Constant.MODULE_CORE_AND_SUBCORE_TYPE))) {
                createCoreAndSubCore(objectTempRequest, programId);
            }

            if (module.getModuleType().equals(sharedMethod.getSettingValue(Constant.MODULE_CURRICULUM_TYPE))) {
                createCurriculum(objectTempRequest, programId);
            }

            if (module.getModuleType().equals(sharedMethod.getSettingValue(Constant.MODULE_CURRICULAR_COMPONENT_TYPE))) {
                createCurricularComponent(objectTempRequest, programId);
            }
        }
    }

    private void createCurriculumSummary(ObjectTempRequest objectTempRequest, Integer programId) throws JsonProcessingException {
        CurriculumSummaryRequest curriculumSummaryRequest = objectMapper
                .readValue(objectTempRequest.getObject(), CurriculumSummaryRequest.class);

        List<CurriculumSummary> curriculumSummaries = curriculumSummaryRepository
                .findCurriculumSummaryByProgramAndType(programId, curriculumSummaryRequest.getCurriculumType());

        if (!curriculumSummaries.isEmpty()) {
            CurriculumSummary curriculumSummary = curriculumSummaries.get(0);
            curriculumSummaryRequest.setCreatedBy(objectTempRequest.getCreatedBy());
            curriculumSummaryRequest.setRoleId(objectTempRequest.getRoleId());
            curriculumSummaryRequest.setWorkflowId(curriculumSummary.getWorkflowBaseStepId().getWorkflowBaseId().getWorkflowBaseId());
            curriculumSummaryRequest.setStepId(curriculumSummary.getWorkflowBaseStepId().getStepId().getStepId());
            workflowService.createCurruculumSummary(curriculumSummaryRequest);

        }
    }

    private void createSyllabus(ObjectTempRequest objectTempRequest, Integer programId) throws JsonProcessingException {
        List<SyllabusRequest> syllabusRequest = objectMapper
                .readValue(objectTempRequest.getObject(), new TypeReference<List<SyllabusRequest>>() {});

        List<Syllabus> syllabusList = syllabusRepository.findSyllabusByProgram(programId);

        for (SyllabusRequest request : syllabusRequest) {
            request.setCreatedBy(objectTempRequest.getCreatedBy());
            request.setWorkflowBaseId(syllabusList.get(0).getWorkflowBaseStepId().getWorkflowBaseId().getWorkflowBaseId());
            request.setStepId(syllabusList.get(0).getWorkflowBaseStepId().getStepId().getStepId());
        }
        workflowService.updateSyllabusMassive(syllabusRequest);
    }

    private void createAcademicCredit(ObjectTempRequest objectTempRequest, Integer programId) throws JsonProcessingException {
        List<SubjectListModel> subjectModelList = objectMapper
                .readValue(objectTempRequest.getObject(), new TypeReference<List<SubjectListModel>>() {});
        for (SubjectListModel model : subjectModelList) {
            model.setCreatedBy(objectTempRequest.getCreatedBy());
        }
        workflowService.updateSubjectCredit(subjectModelList);
        workflowService.calculateParticipationPercentage(programId);
    }

    private void createCoreAndSubCore(ObjectTempRequest objectTempRequest, Integer programId) throws JsonProcessingException {
        List<CoreAndSubcoreModel> coreAndSubcoreModelList = objectMapper
                .readValue(objectTempRequest.getObject(), new TypeReference<List<CoreAndSubcoreModel>>() {});
        for (CoreAndSubcoreModel coreSubcore : coreAndSubcoreModelList) {
            coreSubcore.setCreatedBy(objectTempRequest.getCreatedBy());
        }
        workflowService.updateCoreAndSubcoreMassive(coreAndSubcoreModelList);
    }

    private void createCurriculum(ObjectTempRequest objectTempRequest, Integer programId) throws JsonProcessingException {
        List<CurriculumModel> curriculumModelList = objectMapper
                .readValue(objectTempRequest.getObject(), new TypeReference<List<CurriculumModel>>() {});
        List<CurriculumModel> flattenedList = flatterCurriculum.flattenCurriculumModel(curriculumModelList);
        for (CurriculumModel curriculumModel : flattenedList) {
            curriculumModel.setCreatedBy(objectTempRequest.getCreatedBy());
        }
        workflowService.updateCurriculumMassive(flattenedList);
    }

    private void createCurricularComponent(ObjectTempRequest objectTempRequest, Integer programId) throws JsonProcessingException {
        List<CurricularComponentRequest> curriculumRequestList = objectMapper
                .readValue(objectTempRequest.getObject(), new TypeReference<List<CurricularComponentRequest>>() {});

        List<WorkflowBaseStep> workflowBaseSteps = workflowBaseStepRepository
                .findWorkflowBaseStepByObjectNative(programId);

        if (!workflowBaseSteps.isEmpty()) {
            for (CurricularComponentRequest curricularComponentRequest : curriculumRequestList) {
                SummaryRequest summaryRequest = SummaryRequest.builder()
                        .stepId(curricularComponentRequest.getComponentType())
                        .workflowId(workflowBaseSteps.get(0).getWorkflowBaseId().getWorkflowBaseId())
                        .summary(curricularComponentRequest.getComponentCurricular())
                        .roleId(objectTempRequest.getRoleId())
                        .createdBy(objectTempRequest.getCreatedBy())
                        .build();
                workflowService.updateSummary(summaryRequest);
            }
        }

    }

    private void saveRenovationStatus(ProposalRenovationModules proposalRenovationModules, String createdBy, String status) {
        renovationModuleStatusRepository.save(
                RenovationModuleStatus.builder()
                        .statusId(sharedMethod.getSettingValue(status))
                        .createdBy(createdBy)
                        .createdAt(new Date())
                        .proposalRenovationModulesId(proposalRenovationModules)
                        .build()
        );
    }

    private boolean validateAllModulesApproved(Integer programRenovationId) {
        List<ProposalRenovationModules> proposalRenovationModules = proposalRenovationModuleRepository
                .findAllByProposalRenovationByProgramAndEditable(programRenovationId);
        List<RenovationModuleStatus> renovationModuleStatuses = renovationModuleStatusRepository
                .findAllModulesApproved(programRenovationId);
        return proposalRenovationModules.size() == renovationModuleStatuses.size();
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

    private void updateCurriculum(ObjectTemp objectTemp, ObjectTempRequest objectTempRequest) throws JsonProcessingException {
        List<CurriculumModel> curriculumModelList = objectMapper
                .readValue(objectTemp.getObjectValue(), new TypeReference<List<CurriculumModel>>() {});
        List<CurriculumRequest> curriculumRequestList = objectMapper
                .readValue(objectTempRequest.getObject(), new TypeReference<List<CurriculumRequest>>() {});

        List<CurriculumModel> flattenedList = flatterCurriculum.flattenCurriculumModel(curriculumModelList);
        flattenedList.sort(Comparator.comparing(CurriculumModel::getCurriculumId));

        Map<Integer, CurriculumModel> mapCurriculumModel = new HashMap<>();
        for (CurriculumModel curriculumModel : flattenedList) {
            curriculumModel.setChilds(new ArrayList<>());
            mapCurriculumModel.put(curriculumModel.getCurriculumId(), curriculumModel);
        }

        for (CurriculumRequest curriculumRequest : curriculumRequestList) {
            Integer id = curriculumRequest.getCurriculumId();
            if (mapCurriculumModel.containsKey(id)) {
                CurriculumModel curriculumModel = mapCurriculumModel.get(id);
                curriculumModel.setName(curriculumRequest.getName());
                curriculumModel.setDescription(curriculumRequest.getDescription());
                curriculumModel.setIsUpdated(true);
            }
        }

        flattenedList = new ArrayList<>(mapCurriculumModel.values());
        curriculumModelList = curriculumMapper.mapToTree(flattenedList);
        objectTemp.setObjectValue(gson.toJson(curriculumModelList));
        objectTempRepository.save(objectTemp);
    }

    private void updateSubjectCredits(ObjectTemp objectTemp, ObjectTempRequest objectTempRequest) throws JsonProcessingException {
        List<SubjectListModel> subjectModelList = objectMapper
                .readValue(objectTemp.getObjectValue(), new TypeReference<List<SubjectListModel>>() {});
        List<SubjectRequest> subjectRequestList = objectMapper
                .readValue(objectTempRequest.getObject(), new TypeReference<List<SubjectRequest>>() {});

        Map<Integer, SubjectListModel> mapCurriculumModel = new HashMap<>();
        for (SubjectListModel curriculumModel : subjectModelList) {
            mapCurriculumModel.put(curriculumModel.getSubjectId(), curriculumModel);
        }

        for (SubjectRequest subjectRequest : subjectRequestList) {
            Integer id = subjectRequest.getSubjectId();
            if (mapCurriculumModel.containsKey(id)) {
                SubjectListModel subjectModel = mapCurriculumModel.get(id);
                subjectModel.setCreditNumber(subjectRequest.getCreditNumber());
                subjectModel.setIsUpdated(true);
                subjectModel.setCreatedBy(objectTemp.getCreatedBy());
            }
        }

        subjectModelList = new ArrayList<>(mapCurriculumModel.values());
        subjectModelList.sort(Comparator.comparingInt(SubjectListModel::getSubjectId));
        objectTemp.setObjectValue(gson.toJson(subjectModelList));
        objectTempRepository.save(objectTemp);
    }

    private void updateSyllabus(ObjectTemp objectTemp, ObjectTempRequest objectTempRequest) throws JsonProcessingException {
        List<SyllabusRequest> syllabusModelList = objectMapper
                .readValue(objectTemp.getObjectValue(), new TypeReference<List<SyllabusRequest>>() {});
        List<SyllabusRequest> syllabusRequestList = objectMapper
                .readValue(objectTempRequest.getObject(), new TypeReference<List<SyllabusRequest>>() {});

        Map<Integer, SyllabusRequest> mapSyllabusModel = new HashMap<>();
        for (SyllabusRequest syllabusRequest : syllabusModelList) {
            mapSyllabusModel.put(syllabusRequest.getCurriculumId(), syllabusRequest);
        }

        for (SyllabusRequest syllabusRequest : syllabusRequestList) {
            syllabusRequest.setCreatedBy(objectTemp.getCreatedBy());
            Integer id = syllabusRequest.getCurriculumId();
            mapSyllabusModel.put(id, syllabusRequest);
        }

        syllabusModelList = new ArrayList<>(mapSyllabusModel.values());
        objectTemp.setObjectValue(gson.toJson(syllabusModelList));
        objectTempRepository.save(objectTemp);
    }

    private void updateCurricularSubcore(ObjectTemp objectTemp, ObjectTempRequest objectTempRequest) throws JsonProcessingException {
        List<CoreAndSubcoreModel> coreAndSubcoreModelList = objectMapper
                .readValue(objectTemp.getObjectValue(), new TypeReference<List<CoreAndSubcoreModel>>() {});
        List<CurriculumRequest> curriculumRequestList = objectMapper
                .readValue(objectTempRequest.getObject(), new TypeReference<List<CurriculumRequest>>() {});

        Map<Integer, CoreAndSubcoreModel> mapCpCoreAndSubcoreModel = new HashMap<>();
        for (CoreAndSubcoreModel coreAndSubcoreModel : coreAndSubcoreModelList) {
            mapCpCoreAndSubcoreModel.put(coreAndSubcoreModel.getCurriculumId(), coreAndSubcoreModel);
        }

        for (CurriculumRequest curriculumRequest : curriculumRequestList) {
            Integer id = curriculumRequest.getCurriculumId();
            if (mapCpCoreAndSubcoreModel.containsKey(id)) {
                CoreAndSubcoreModel coreAndSubcoreModel = mapCpCoreAndSubcoreModel.get(id);
                coreAndSubcoreModel.setName(curriculumRequest.getName());
                coreAndSubcoreModel.setDescription(curriculumRequest.getDescription());
                coreAndSubcoreModel.setCode(curriculumRequest.getCode());
                coreAndSubcoreModel.setRaeg(curriculumRequest.getRaeg());
                coreAndSubcoreModel.setHoursInteractionTeacher(curriculumRequest.getHoursInteractionTeacher());
                coreAndSubcoreModel.setHourSelfWork(curriculumRequest.getHourSelfWork());
                coreAndSubcoreModel.setIsUpdated(true);
            }
        }

        coreAndSubcoreModelList = new ArrayList<>(mapCpCoreAndSubcoreModel.values());
        objectTemp.setObjectValue(gson.toJson(coreAndSubcoreModelList));
        objectTempRepository.save(objectTemp);
    }

    private void updateCurricularComponent(ObjectTemp objectTemp, ObjectTempRequest objectTempRequest) throws JsonProcessingException {
        List<CurricularComponentRequest> curricularComponentList = objectMapper
                .readValue(objectTemp.getObjectValue(), new TypeReference<List<CurricularComponentRequest>>() {});
        List<CurricularComponentRequest> curricularRequestList = objectMapper
                .readValue(objectTempRequest.getObject(), new TypeReference<List<CurricularComponentRequest>>() {});

        Map<Integer, CurricularComponentRequest> mapCurricularComponentModel = new HashMap<>();
        for (CurricularComponentRequest curriculumRequest : curricularComponentList) {
            mapCurricularComponentModel.put(curriculumRequest.getComponentType(), curriculumRequest);
        }

        for (CurricularComponentRequest curriculumRequest : curricularRequestList) {
            Integer id = curriculumRequest.getComponentType();
            if (mapCurricularComponentModel.containsKey(id)) {
                CurricularComponentRequest curricularComponentRequest = mapCurricularComponentModel.get(id);
                curricularComponentRequest.setComponentCurricular(curriculumRequest.getComponentCurricular());
            }
        }

        curricularComponentList = new ArrayList<>(mapCurricularComponentModel.values());
        objectTemp.setObjectValue(gson.toJson(curricularComponentList));
        objectTemp.setCreatedBy(objectTempRequest.getCreatedBy());
        objectTempRepository.save(objectTemp);
    }

}
