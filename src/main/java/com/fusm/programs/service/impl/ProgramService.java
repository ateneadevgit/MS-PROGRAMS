package com.fusm.programs.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fusm.programs.dto.*;
import com.fusm.programs.entity.Campus;
import com.fusm.programs.entity.Modality;
import com.fusm.programs.entity.Program;
import com.fusm.programs.entity.Status;
import com.fusm.programs.external.*;
import com.fusm.programs.model.*;
import com.fusm.programs.model.external.*;
import com.fusm.programs.repository.*;
import com.fusm.programs.service.IProgramService;
import com.fusm.programs.util.Constant;
import com.fusm.programs.util.SharedMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProgramService implements IProgramService {

    @Autowired
    private IProgramRepository programRepository;

    @Autowired
    private ISettingsService settingsService;

    @Autowired
    private IStatusRepository statusRepository;

    @Autowired
    private ICampusRepository campusRepository;

    @Autowired
    private IModalityRepository modalityRepository;

    @Autowired
    private ProgramCustomRepository programCustomRepository;

    @Autowired
    private ISafetyMeshService safetyMeshService;

    @Autowired
    private ICatalogService catalogService;

    @Autowired
    private INotificationService notificationService;

    @Autowired
    private IWorkflowService workflowService;

    @Autowired
    private SharedMethod sharedMethod;

    ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public List<ProgramDto> getPrograms(Integer facultyId) {
        return programRepository.getProgramList(
                getSettingValue(Constant.STATUS_PROGRAM),
                getSettingValue(Constant.PROGRAM_APPROVED),
                (facultyId == 0) ? null : facultyId,
                null
        );
    }

    @Override
    public List<ProgramDto> getProgramsByFaculty(List<Integer> facultyIds) {
        return programRepository.getProgramList(
                getSettingValue(Constant.STATUS_PROGRAM),
                getSettingValue(Constant.PROGRAM_APPROVED),
                null,
                null
        ).stream().filter(programDto -> facultyIds.contains(programDto.getFacultyId()))
                .collect(Collectors.toList());
    }

    @Override
    public List<TecnicalProgramDto> getTecnicalPrograms(Integer facultyId) {
        return programRepository.getTecnicalProgramList(
                getSettingValue(Constant.STATUS_PROGRAM),
                getSettingValue(Constant.PROGRAM_APPROVED),
                (facultyId == 0) ? null : facultyId,
                null
        );
    }

    @Override
    public ResponsePage<String> getProgramsByStatus(
            QueryFilterProposal queryFilterProposal, PageModel pageModel, String status, RoleRequest roleRequest
    ) throws JsonProcessingException {

        String programStatus = null;
        String module = null;
        if (roleRequest.getFacultyId() != null) queryFilterProposal.setFacultyId(roleRequest.getFacultyId());

        if (status.equals("active")) {
            programStatus = Constant.PROGRAM_APPROVED;
            module = Constant.PROGRAM_ACTIVE_MODULE;
        }
        if (status.equals("declined")) {
            programStatus = Constant.PROGRAM_DECLINED;
            module = Constant.PROGRAM_DECLINED_MODULE;
        }
        if (status.equals("review")) {
            programStatus = Constant.PROGRAM_ON_CONSTRUCTION;
            module = Constant.PROGRAM_ON_CONST_MODULE;
        }
        if (status.equals("disabled")) {
            programStatus = Constant.PROGRAM_DISABLED;
            module = Constant.PROGRAM_DISABLED_MODULE;
        }

        return executeDynamicSelect(
                queryFilterProposal,
                pageModel,
                roleRequest,
                getSettingValue(Constant.STATUS_PROGRAM),
                getSettingValue(programStatus),
                getSettingValue(module));
    }

    @Override
    public ResponsePage<String> getProposal(QueryFilterProposal queryFilterProposal, PageModel pageModel, RoleRequest roleRequest
    ) throws JsonProcessingException {
        if (roleRequest.getFacultyId() != null) queryFilterProposal.setFacultyId(roleRequest.getFacultyId());
        return executeDynamicSelect(
                queryFilterProposal,
                pageModel,
                roleRequest,
                getSettingValue(Constant.STATUS_PROPOSAL),
                null,
                getSettingValue(Constant.PROPOSAL_MODULE));
    }

    @Override
    @Transactional
    public void createProposal(ProgramRequestModel programRequestModel) {
        Program programFather = null;
        if (programRequestModel.getIsEnlarge()) {
            Optional<Program> programFatherOptional = programRepository.findById(programRequestModel.getProgramFather());
            if (programFatherOptional.isPresent()) programFather = programFatherOptional.get();
        }
        String fileRoute = sharedMethod.saveFile(programRequestModel.getFileProposal(), programRequestModel.  getCreatedBy());
        Program program = Program.builder()
                .logo((programFather != null) ? programFather.getLogo() :
                        sharedMethod.saveFile(programRequestModel.getLogo(), programRequestModel.getCreatedBy()))
                .cover((programFather != null) ? programFather.getCover() :
                        sharedMethod.saveFile(programRequestModel.getCover(), programRequestModel.getCreatedBy()))
                .name(programRequestModel.getProgramName())
                .facultyId((programFather != null) ? programFather.getFacultyId() :
                        programRequestModel.getFacultyId())
                .typeFormationId((programFather != null) ? programFather.getTypeFormationId() :
                        programRequestModel.getFormationTypeId())
                .levelFormationId((programFather != null) ? programFather.getLevelFormationId() :
                        programRequestModel.getFormationLevel())
                .ansId(getSettingValue(Constant.DEFAULT_ANS))
                .developmentDate(programRequestModel.getDevelopmentDate())
                .fileUrl(fileRoute)
                .isEnlarge(programRequestModel.getIsEnlarge())
                .registryTypeId(programRequestModel.getRegistryTypeId())
                .createdBy(programRequestModel.getCreatedBy())
                .createdAt(new Date(System.currentTimeMillis() - 10 * 60 * 60 * 1000))
                .enabled(true)
                .programFather(programFather)
                .build();

        sendNotification(
                programRequestModel.getFacultyId(),
                programRequestModel.getCreatedBy().split(","),
                programRequestModel.getProgramName(),
                Constant.PROPOSAL_CREATION_TEMPLATE);

        sendNotification(
                programRequestModel.getFacultyId(),
                getSettingValueToString(Constant.VICERRECTOR_EMAIL).split(","),
                programRequestModel.getProgramName(),
                Constant.REQUEST_PROPOSAL_CREATION_TEMPLATE);

        programRepository.save(program);

        if (!program.getIsEnlarge()) {
            saveStatus(programRequestModel.getCreatedBy(), programRequestModel.getRoleId(), program, fileRoute);
        } else {
            saveStatus(programRequestModel.getCreatedBy(), programRequestModel.getRoleId(), program, fileRoute);
            saveProposalRenovationStatus(programRequestModel.getCreatedBy(), programRequestModel.getRoleId(), programFather, fileRoute);
        }

        saveModality(programRequestModel.getModality(), program);
        saveCampus(programRequestModel.getCampus(), program);
    }

    @Override
    public void updateProposal(ProgramUpdateModel programUpdateModel, Integer programId) {
        Optional<Program> programOptional = programRepository.findById(programId);
        if (programOptional.isPresent()) {
            String fileRoute = sharedMethod.saveFile(programUpdateModel.getFileProposal(), programUpdateModel.getCreatedBy());
            Program program = programOptional.get();
            program.setFileUrl(fileRoute);
            program.setUpdatedAt(new Date(System.currentTimeMillis() - 10 * 60 * 60 * 1000));
            programRepository.save(program);
            saveStatus(programUpdateModel.getCreatedBy(), programUpdateModel.getRoleId(), program, fileRoute);

            sendNotification(
                    program.getFacultyId(),
                    getSettingValueToString(Constant.VICERRECTOR_EMAIL).split(","),
                    program.getName(),
                    Constant.UPDATE_PROPOSAL_TEMPLATE);
        }
    }

    @Override
    public ProgramIdDto getProgramById(Integer programId) {
        return programRepository.getProgramByIdNative(programId);
    }

    @Override
    public List<HistoricDto> getFeedbackProposalHistoric(Integer proposalId) {
        return programRepository.getProposalHistoric(proposalId, getSettingValue(Constant.STATUS_PROPOSAL));
    }

    @Override
    public boolean getIsFormal(Integer programId) {
        boolean isFormal = false;
        Optional<Program> programOptional = programRepository.findById(programId);
        if (programOptional.isPresent()) {
            isFormal = (programOptional.get().getTypeFormationId() == 15);
        }
        return isFormal;
    }

    @Override
    public Integer getUpdateStatus(Integer programId) {
        List<Status> programUpdateStatus = statusRepository.finsLastUpgradeStatus(programId);
        return (programUpdateStatus.isEmpty()) ? null : programUpdateStatus.get(0).getStatusId();
    }

    @Override
    public Date getApprovedProposalDate(Integer programId) {
        return statusRepository.findApprovedProposalDate(programId).get(0);
    }

    @Override
    public List<OwnProgramDto> getProgramAcademic(QueryFilterProposal queryFilterProposal) {
        return programRepository.findOwnPrograms(
                queryFilterProposal.getLevelFormation(),
                queryFilterProposal.getFacultyId(),
                queryFilterProposal.getCampusId(),
                queryFilterProposal.getModalityId()
        );
    }

    private Integer getSettingValue(String settingName) {
        return Integer.parseInt(
                settingsService.getSetting(
                        SettingRequest.builder()
                                .settingName(settingName)
                                .build()
                )
        );
    }

    private String getSettingValueToString(String settingName) {
        return settingsService.getSetting(
                        SettingRequest.builder()
                                .settingName(settingName)
                                .build()
                );
    }

    private void saveStatus(String createdBy, Integer roleId, Program program, String fileRoute) {
        statusRepository.save(
                Status.builder()
                        .statusId(getSettingValue(Constant.PROPOSAL_REQUEST_SENT))
                        .statusType(getSettingValue(Constant.STATUS_PROPOSAL))
                        .createdAt(new Date(System.currentTimeMillis() - 10 * 60 * 60 * 1000))
                        .createdBy(createdBy)
                        .programId(program)
                        .roleId(roleId)
                        .feedbackFileUrl(fileRoute)
                        .build()
        );
    }

    private void saveCampus(Integer[] campusId, Program program) {
        for (Integer campus : campusId) {
            campusRepository.save(
                    Campus.builder()
                            .value(campus)
                            .enabled(true)
                            .programId(program)
                            .build()
            );
        }
    }

    private void saveModality(Integer[] modalityId, Program program) {
        for (Integer modality : modalityId) {
            modalityRepository.save(
                    Modality.builder()
                            .value(modality)
                            .enabled(true)
                            .programId(program)
                            .build()
            );
        }
    }

    private ResponsePage<String> executeDynamicSelect(
            QueryFilterProposal queryFilterProposal, PageModel pageModel, RoleRequest roleRequest, Integer programType, Integer statusId, Integer moduleId
        ) throws JsonProcessingException {

        List<ColumnPermission> columns = safetyMeshService.getColumnsByRoleAndModule(
                new ValidatePermission(roleRequest.getRoleId(), moduleId)
        );

        Integer[] programIds = defineRoleAndStatus(roleRequest.getRoleId(), statusId, roleRequest.getCreatedBy());
        boolean hasRestriction = false;

        if (programIds != null && statusId.equals(getSettingValue(Constant.PROGRAM_APPROVED))) {
            hasRestriction = true;
        }

        String columnNames = columnNames(columns);
        List<Map<String, Object>> resultComplete = programCustomRepository
                .callDynamicSelect(
                        columnNames, programType, statusId, queryFilterProposal.getFacultyId(), queryFilterProposal.getCampusId(), columnDataType(columns), hasRestriction, programIds);
        List<Map<String, Object>> result = programCustomRepository
                .callDynamicPagingSelect(
                        columnNames,
                        programType,
                        statusId,
                        queryFilterProposal.getFacultyId(),
                        queryFilterProposal.getCampusId(),
                        columnDataType(columns),
                        hasRestriction,
                        pageModel.getPage(),
                        pageModel.getSize(),
                        programIds);

        List<String> response = new ArrayList<>();
        for (Map<String, Object> rs : result) {
            String json = objectMapper.writeValueAsString(rs);
            response.add(json);
        }

        return pageProgram(resultComplete, response , pageModel.getPage(), pageModel.getSize());
    }

    private ResponsePage<String> pageProgram(
            List<Map<String, Object>> resultComplete, List<String> programList, Integer page, Integer size) {

         return ResponsePage.<String>builder()
                .numberOfPage(page)
                .itemsPerPage(size)
                .itemsOnThisPage(programList.size())
                .totalNumberPages(resultComplete.size() / size)
                .totalNumberItems((long) resultComplete.size())
                .hasNextPage((page * size) < resultComplete.size())
                .hasPreviousPage(page != 1)
                 .content(programList)
                .build();
    }

    private String columnNames(List<ColumnPermission> columns) {
        StringBuilder result = new StringBuilder();
        for (ColumnPermission column : columns) {
            if (column.getTableOrigin() != null && !column.getTableOrigin().isEmpty()) {
                result.append(column.getTableOrigin()).append(".");
            }
            result.append(column.getColumnName()).append(", ");
        }
        result.deleteCharAt(result.length() - 2);
        return result.toString();
    }

    private String columnDataType(List<ColumnPermission> columns) {
        StringBuilder result = new StringBuilder();
        for (ColumnPermission column : columns) {
            result.append(column.getColumnName()).append(" ").append(column.getDataType()).append(", ");
        }
        result.deleteCharAt(result.length() - 2);
        return result.toString();
    }

    private void sendNotification(Integer facultyId, String[] sendTo, String programName, String templateString) {
        Integer programCreationTemplateId = getSettingValue(templateString);
        Template programCreationTemplate = notificationService.getTemplate(programCreationTemplateId);
        String faculty = catalogService.getCatalogItemValue(facultyId);
        String template  = programCreationTemplate.getEmailBody();

        template = template.replace(Constant.PROGRAM_FLAG, programName);
        template = template.replace(Constant.FACULTY_FLAG, faculty);
        template = template.replace(Constant.URL_FLAG, getSettingValueToString(Constant.ATENEA_URL));

        notificationService.sendNotification(
                NotificationRequest.builder()
                        .sendTo(sendTo)
                        .subject(programCreationTemplate.getSubject().replace(Constant.PROGRAM_FLAG, programName))
                        .body(template)
                        .build()
        );
    }

    private Integer[]  defineRoleAndStatus(Integer roleId, Integer status, String userEmail) {
        Integer[] programIds = null;
        Integer onConstructionValue = getSettingValue(Constant.PROGRAM_ON_CONSTRUCTION);
        Integer activeValue = getSettingValue(Constant.PROGRAM_APPROVED);

        if ((roleId.equals(Constant.DIR_ROLE) && status.equals(onConstructionValue)) || (roleId.equals(Constant.DIR_ROLE) && status.equals(activeValue))) {
            List<Integer> programIdList = workflowService.getUserWithPermissionInWorkflow(userEmail, Constant.DIR_ROLE);
            programIds = programIdList.toArray(new Integer[0]);
        } else if ((roleId.equals(Constant.AC_ROLE) && status.equals(onConstructionValue)) || (roleId.equals(Constant.AC_ROLE) && status.equals(activeValue))) {
            List<Integer> programIdList = workflowService.getUserWithPermissionInWorkflow(userEmail, Constant.AC_ROLE);
            programIds = programIdList.toArray(new Integer[0]);
        }

        return programIds;
    }

    private void saveProposalRenovationStatus(String createdBy, Integer roleId, Program program, String fileRoute) {
        statusRepository.save(
                Status.builder()
                        .statusId(getSettingValue(Constant.RENOVATION_REQUESTS_STATUS))
                        .statusType(getSettingValue(Constant.STATUS_RENOVATION))
                        .createdAt(new Date(System.currentTimeMillis() - 10 * 60 * 60 * 1000))
                        .createdBy(createdBy)
                        .programId(program)
                        .roleId(roleId)
                        .feedbackFileUrl(fileRoute)
                        .build()
        );
    }

}
