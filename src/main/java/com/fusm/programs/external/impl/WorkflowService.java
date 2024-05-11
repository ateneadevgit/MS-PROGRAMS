package com.fusm.programs.external.impl;

import com.fusm.programs.external.IWorkflowService;
import com.fusm.programs.model.*;
import com.fusm.programs.model.external.CoreAndSubcoreModel;
import com.fusm.programs.model.external.CurriculumModel;
import com.fusm.programs.model.external.CurriculumSummaryModel;
import com.fusm.programs.model.external.SubjectListModel;
import com.fusm.programs.webclient.WebClientConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class WorkflowService implements IWorkflowService {

    @Autowired
    private WebClientConnector webClientConnector;

    @Value("${ms-workflow.complete-path}")
    private String WORKFLOW_ROUTE;

    @Value("${ms-workflow.workflow.path}")
    private String WORKFLOW_SERVICE;


    @Override
    public List<Integer> getUserWithPermissionInWorkflow(String userEmail, Integer roleId) {
        return webClientConnector.connectWebClient(WORKFLOW_ROUTE)
                .get()
                .uri(WORKFLOW_SERVICE + "/user/" + userEmail + "/role-id/" + roleId)
                .retrieve()
                .bodyToFlux(Integer.class)
                .collectList()
                .block();
    }

    @Override
    public void createCurruculumSummary(CurriculumSummaryRequest curriculumSummaryRequest) {
        webClientConnector.connectWebClient(WORKFLOW_ROUTE)
                .post()
                .uri(WORKFLOW_SERVICE + "/curriculum/summary/with-history")
                .bodyValue(curriculumSummaryRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public void createSyllabus(SyllabusRequest syllabusRequest) {
        webClientConnector.connectWebClient(WORKFLOW_ROUTE)
                .post()
                .uri(WORKFLOW_SERVICE + "/syllabus")
                .bodyValue(syllabusRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public void updateCurriculum(CurriculumRequest curriculumRequest) {
        webClientConnector.connectWebClient(WORKFLOW_ROUTE)
                .put()
                .uri(WORKFLOW_SERVICE + "/curriculum/name-description")
                .bodyValue(curriculumRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public void updateSubjectCredit(List<SubjectListModel> subjectListModelList) {
        webClientConnector.connectWebClient(WORKFLOW_ROUTE)
                .put()
                .uri(WORKFLOW_SERVICE + "/curriculum/credit")
                .bodyValue(subjectListModelList)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public void calculateParticipationPercentage(Integer programId) {
        webClientConnector.connectWebClient(WORKFLOW_ROUTE)
                .get()
                .uri(WORKFLOW_SERVICE + "/curriculum/object-id/" + programId + "/calculate/percentage")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public void updateSummary(SummaryRequest summaryRequest) {
        webClientConnector.connectWebClient(WORKFLOW_ROUTE)
                .put()
                .uri(WORKFLOW_SERVICE + "/summary")
                .bodyValue(summaryRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public List<CurriculumModel> getCurriculum(Integer objectId) {
        return webClientConnector.connectWebClient(WORKFLOW_ROUTE)
                .get()
                .uri(WORKFLOW_SERVICE + "/curriculum/object-id/" + objectId)
                .retrieve()
                .bodyToFlux(CurriculumModel.class)
                .collectList()
                .block();
    }

    @Override
    public List<SubjectListModel> getSubjects(Integer programId) {
        return webClientConnector.connectWebClient(WORKFLOW_ROUTE)
                .get()
                .uri(WORKFLOW_SERVICE + "/curriculum/object-id/" + programId + "/subjects")
                .retrieve()
                .bodyToFlux(SubjectListModel.class)
                .collectList()
                .block();
    }

    @Override
    public void updateCurriculumMassive(List<CurriculumModel> curriculumModelList) {
        webClientConnector.connectWebClient(WORKFLOW_ROUTE)
                .put()
                .uri(WORKFLOW_SERVICE + "/curriculum/massive")
                .bodyValue(curriculumModelList)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public List<CurriculumModel> getCurriculumWithoutCore(Integer objectId) {
        return webClientConnector.connectWebClient(WORKFLOW_ROUTE)
                .get()
                .uri(WORKFLOW_SERVICE + "/curriculum/object-id/" + objectId + "/no-core")
                .retrieve()
                .bodyToFlux(CurriculumModel.class)
                .collectList()
                .block();
    }

    @Override
    public void updateSyllabusMassive(List<SyllabusRequest> syllabusRequestList) {
        webClientConnector.connectWebClient(WORKFLOW_ROUTE)
                .put()
                .uri(WORKFLOW_SERVICE + "/syllabus/massive")
                .bodyValue(syllabusRequestList)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public CurriculumModel getCurriculumById(Integer curriculumId) {
        return webClientConnector.connectWebClient(WORKFLOW_ROUTE)
                .get()
                .uri(WORKFLOW_SERVICE + "/curriculum/" + curriculumId)
                .retrieve()
                .bodyToMono(CurriculumModel.class)
                .block();
    }

    @Override
    public List<CoreAndSubcoreModel> getCoreAndSubcore(Integer programId) {
        return webClientConnector.connectWebClient(WORKFLOW_ROUTE)
                .get()
                .uri(WORKFLOW_SERVICE + "/curriculum/object-id/" + programId + "/core")
                .retrieve()
                .bodyToFlux(CoreAndSubcoreModel.class)
                .collectList()
                .block();
    }

    @Override
    public void updateCoreAndSubcoreMassive(List<CoreAndSubcoreModel> coreAndSubcoreModels) {
        webClientConnector.connectWebClient(WORKFLOW_ROUTE)
                .put()
                .uri(WORKFLOW_SERVICE + "/curriculum/core-subcore")
                .bodyValue(coreAndSubcoreModels)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    @Override
    public CurriculumSummaryModel getCurriculumSummaryByProgram(Integer programId, Integer type) {
        return webClientConnector.connectWebClient(WORKFLOW_ROUTE)
                .get()
                .uri(WORKFLOW_SERVICE + "/curriculum/summary/type/" + type + "/object-id/" + programId)
                .retrieve()
                .bodyToMono(CurriculumSummaryModel.class)
                .block();
    }

}
