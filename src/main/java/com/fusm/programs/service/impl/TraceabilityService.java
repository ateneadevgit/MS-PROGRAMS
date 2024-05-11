package com.fusm.programs.service.impl;

import com.fusm.programs.entity.Campus;
import com.fusm.programs.entity.Minute;
import com.fusm.programs.entity.Program;
import com.fusm.programs.entity.Traceability;
import com.fusm.programs.external.IDocumentManagerService;
import com.fusm.programs.model.*;
import com.fusm.programs.model.external.DocumentRequest;
import com.fusm.programs.repository.ICampusRepository;
import com.fusm.programs.repository.IMinuteRepository;
import com.fusm.programs.repository.IProgramRepository;
import com.fusm.programs.repository.ITraceabilityRepository;
import com.fusm.programs.service.ICampusService;
import com.fusm.programs.service.ITraceabilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TraceabilityService implements ITraceabilityService {

    @Autowired
    private IProgramRepository programRepository;

    @Autowired
    private ITraceabilityRepository traceabilityRepository;

    @Autowired
    private IMinuteRepository minuteRepository;

    @Autowired
    private ICampusRepository campusRepository;

    @Autowired
    private ICampusService campusService;

    @Autowired
    private IDocumentManagerService documentManagerService;


    @Override
    public TraceabilityRequest<Object> getTraceabilityByProgramId(Integer programId) {
        Optional<Program> program = programRepository.findById(programId);
        TraceabilityRequest<Object> traceabilityResponse = new TraceabilityRequest<>();

        if (program.isPresent()) {
            Traceability traceability = program.get().getTraceabilityId();
            if (traceability != null) {
                traceabilityResponse = TraceabilityRequest.builder()
                        .proposalAprovedDate(traceability.getProposalApprovedDate())
                        .superiorCouncilMinute(
                                MinuteRequest.builder()
                                        .minute((traceability.getSuperiorCouncilMinuteId() != null) ?
                                                traceability.getSuperiorCouncilMinuteId().getMinute() : null)
                                        .minuteDate((traceability.getSuperiorCouncilMinuteId() != null) ?
                                                traceability.getSuperiorCouncilMinuteId().getMinuteDate() : null)
                                        .build()
                        )
                        .menEndDate(traceability.getMenEndDate())
                        .viceAcademicMinute(
                                MinuteRequest.builder()
                                        .minute((traceability.getViceAcademicMinuteId() != null) ?
                                                traceability.getViceAcademicMinuteId().getMinute() : null)
                                        .minuteDate((traceability.getViceAcademicMinuteId() != null) ?
                                                traceability.getViceAcademicMinuteId().getMinuteDate() : null)
                                        .build()
                        )
                        .nsacesDate(traceability.getNsacesDate())
                        .sinies(traceability.getSinies())
                        .academicCouncilMinute(
                                MinuteRequest.builder()
                                        .minute((traceability.getAcademicCouncilMinuteId() != null) ?
                                                traceability.getAcademicCouncilMinuteId().getMinute() : null)
                                        .minuteDate((traceability.getAcademicCouncilMinuteId() != null) ?
                                                traceability.getAcademicCouncilMinuteId().getMinuteDate() : null)
                                        .build()
                        )
                        .campus(getCampusApproved(programId))
                        .approvedMinute(traceability.getApprovedMinute())
                        .build();
            }
        };

        return traceabilityResponse;
    }

    @Override
    public void createTraceability(TraceabilityRequest<FileModel> traceabilityRequest, Integer programId) {
        Optional<Program> programOptional = programRepository.findById(programId);

        if (programOptional.isPresent()) {
            Program program = programOptional.get();

            Traceability traceability = traceabilityRepository.save(
                    Traceability.builder()
                            .proposalApprovedDate(traceabilityRequest.getProposalAprovedDate())
                            .superiorCouncilMinuteId((traceabilityRequest.getSuperiorCouncilMinute() != null) ?
                                    saveMinute(traceabilityRequest.getSuperiorCouncilMinute(), traceabilityRequest.getCreatedBy()) : null)
                            .menEndDate(traceabilityRequest.getMenEndDate())
                            .viceAcademicMinuteId((traceabilityRequest.getViceAcademicMinute() != null) ?
                                    saveMinute(traceabilityRequest.getViceAcademicMinute(), traceabilityRequest.getCreatedBy()) : null)
                            .nsacesDate(traceabilityRequest.getNsacesDate())
                            .sinies(traceabilityRequest.getSinies())
                            .academicCouncilMinuteId((traceabilityRequest.getAcademicCouncilMinute() != null) ?
                                    saveMinute(traceabilityRequest.getAcademicCouncilMinute(), traceabilityRequest.getCreatedBy()) : null)
                            .approvedMinute((traceabilityRequest.getApprovedMinute() != null) ?
                                    fileRoute(traceabilityRequest.getApprovedMinute(), traceabilityRequest.getCreatedBy()) : null)
                            .build()
            );

            program.setTraceabilityId(traceability);
            program.setUpdatedAt(new Date());
            programRepository.save(program);

            approveCampus(traceabilityRequest.getCampus(), programId);
        }
    }

    @Override
    public void updateTraceability(TraceabilityRequest<FileModel> traceabilityRequest, Integer programId) {
        Optional<Program> programOptional = programRepository.findById(programId);

        if (programOptional.isPresent()) {
            Program program = programOptional.get();
            Traceability traceability = program.getTraceabilityId();
            traceability.setProposalApprovedDate(traceabilityRequest.getProposalAprovedDate());
            traceability.setSuperiorCouncilMinuteId((traceabilityRequest.getSuperiorCouncilMinute() != null) ?
                    updateMinute(traceabilityRequest.getSuperiorCouncilMinute(), traceability.getSuperiorCouncilMinuteId(), traceabilityRequest.getCreatedBy()) : null);
            traceability.setMenEndDate(traceabilityRequest.getMenEndDate());
            traceability.setViceAcademicMinuteId((traceabilityRequest.getViceAcademicMinute() != null) ?
                    updateMinute(traceabilityRequest.getViceAcademicMinute(), traceability.getViceAcademicMinuteId(), traceabilityRequest.getCreatedBy()) : null);
            traceability.setNsacesDate(traceabilityRequest.getNsacesDate());
            traceability.setSinies(traceabilityRequest.getSinies());
            traceability.setAcademicCouncilMinuteId((traceabilityRequest.getAcademicCouncilMinute() != null) ?
                    updateMinute(traceabilityRequest.getAcademicCouncilMinute(), traceability.getAcademicCouncilMinuteId(), traceabilityRequest.getCreatedBy()) : null);
            approveCampus(traceabilityRequest.getCampus(), program.getProgramId());
        }
    }

    private Minute saveMinute(MinuteRequest minuteRequest, String createdBy) {
        return minuteRepository.save(
          Minute.builder()
                  .minute(minuteRequest.getMinute())
                  .minuteDate(minuteRequest.getMinuteDate())
                  .createdAt(new Date())
                  .createdBy(createdBy)
                  .build()
        );
    }

    private Minute updateMinute(MinuteRequest minuteRequest, Minute minute, String createdBy) {
        if (minute != null) {
            minute.setMinute(minuteRequest.getMinute());
            minute.setMinuteDate(minuteRequest.getMinuteDate());
        } else {
            minute = saveMinute(minuteRequest, createdBy);
        }
       return minuteRepository.save(minute);
    }

    private void approveCampus(List<CampusRequest> campusRqList, Integer programId) {
        List<Integer> approvedCampus = new ArrayList<>();

        for (CampusRequest campusRq : campusRqList) {
            List<Campus> campusOptional = campusRepository.findAllByValueAndProgramId_ProgramId(campusRq.getCampusId(), programId);
            if (!campusOptional.isEmpty()) {
                Campus campus = campusOptional.get(0);
                campus.setResolution(campusRq.getResolution());
                campus.setResolutionDate(campusRq.getResolutionDate());
                campus.setEnabled(true);
                approvedCampus.add(campus.getValue());
                campusRepository.save(campus);
            }
        }
        campusService.disableCampus(approvedCampus, programId);
    }

    private List<CampusRequest> getCampusApproved(Integer programId) {
        List<Campus> campusList = campusRepository.findAllByProgramId_ProgramIdAndEnabled(programId, true);
        List<CampusRequest> campusRequestList = new ArrayList<>();

        for (Campus campus : campusList) {
            campusRequestList.add(
                    CampusRequest.builder()
                            .campusId(campus.getValue())
                            .resolution(campus.getResolution())
                            .resolutionDate(campus.getResolutionDate())
                            .build()
            );
        }

        return campusRequestList;
    }

    private String fileRoute(FileModel fileModel, String userEmail) {
        return documentManagerService.saveFile(
                DocumentRequest.builder()
                        .documentBytes(fileModel.getFileContent())
                        .documentExtension(fileModel.getFileExtension())
                        .documentVersion("1")
                        .idUser(userEmail)
                        .build()
        );
    }

}
