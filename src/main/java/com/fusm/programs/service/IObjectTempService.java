package com.fusm.programs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fusm.programs.model.EvaluateObjectRequest;
import com.fusm.programs.model.ObjectTempModel;
import com.fusm.programs.model.ObjectTempRequest;
import com.fusm.programs.model.ObjectToReview;
import com.fusm.programs.model.external.CoreAndSubcoreModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IObjectTempService {

    void createdObjectTemp(ObjectTempRequest objectTempRequest, Integer programId) throws JsonProcessingException;
    void evaluateObject(EvaluateObjectRequest evaluateObjectRequest, Integer programId) throws JsonProcessingException;
    void sendObjectToReview(ObjectToReview objectToReview) throws JsonProcessingException;
    ObjectTempModel getObjectTemp(Integer programId, Integer moduleId);
    void updateObjectTemp(ObjectTempRequest objectTempRequest, Integer programId) throws JsonProcessingException;
    int getObjectNumberCredits(Integer moduleId, Integer programId) throws JsonProcessingException;
    List<CoreAndSubcoreModel> getCores(Integer moduleId, Integer programId) throws JsonProcessingException;
    List<CoreAndSubcoreModel> getSubjectsByCore(Integer coreId, Integer moduleId, Integer programId) throws JsonProcessingException;

}
