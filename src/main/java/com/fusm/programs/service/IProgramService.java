package com.fusm.programs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fusm.programs.dto.*;
import com.fusm.programs.model.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public interface IProgramService {

    List<ProgramDto> getPrograms(Integer facultyId);
    List<ProgramDto> getProgramsByFaculty(List<Integer> facultyId);
    List<TecnicalProgramDto> getTecnicalPrograms(Integer facultyId);
    ResponsePage<String> getProgramsByStatus(QueryFilterProposal queryFilterProposal, PageModel pageModel, String status, RoleRequest roleRequest) throws JsonProcessingException;
    ResponsePage<String> getProposal(QueryFilterProposal queryFilterProposal, PageModel pageModel, RoleRequest roleRequest) throws JsonProcessingException;
    void createProposal(ProgramRequestModel programRequestModel);
    void updateProposal(ProgramUpdateModel programUpdateModel, Integer programId);
    ProgramIdDto getProgramById(Integer programId);
    List<HistoricDto> getFeedbackProposalHistoric(Integer proposalId);
    boolean getIsFormal(Integer programId);
    Integer getUpdateStatus(Integer programId);
    Date getApprovedProposalDate(Integer programId);
    List<OwnProgramDto> getProgramAcademic(QueryFilterProposal queryFilterProposal);

}
