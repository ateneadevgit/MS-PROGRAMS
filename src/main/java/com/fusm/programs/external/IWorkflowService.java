package com.fusm.programs.external;

import com.fusm.programs.model.*;
import com.fusm.programs.model.external.CoreAndSubcoreModel;
import com.fusm.programs.model.external.CurriculumModel;
import com.fusm.programs.model.external.CurriculumSummaryModel;
import com.fusm.programs.model.external.SubjectListModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IWorkflowService {

    List<Integer> getUserWithPermissionInWorkflow(String userEmail, Integer roleId);
    void createCurruculumSummary(CurriculumSummaryRequest curriculumSummaryRequest);
    void createSyllabus(SyllabusRequest syllabusRequest);
    void updateCurriculum(CurriculumRequest curriculumRequest);
    void updateSubjectCredit(List<SubjectListModel> subjectListModelList);
    void calculateParticipationPercentage(Integer programId);
    void updateSummary(SummaryRequest summaryRequest);
    List<CurriculumModel> getCurriculum(Integer objectId);
    List<SubjectListModel> getSubjects(Integer programId);
    void updateCurriculumMassive(List<CurriculumModel> curriculumModelList);
    List<CurriculumModel> getCurriculumWithoutCore(Integer objectId);
    void updateSyllabusMassive(List<SyllabusRequest> syllabusRequestList);
    CurriculumModel getCurriculumById(Integer curriculumId);
    List<CoreAndSubcoreModel> getCoreAndSubcore(Integer programId);
    void updateCoreAndSubcoreMassive(List<CoreAndSubcoreModel> coreAndSubcoreModels);
    CurriculumSummaryModel getCurriculumSummaryByProgram(Integer programId, Integer type);

}
