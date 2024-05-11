package com.fusm.programs.service;

import com.fusm.programs.model.FileModel;
import com.fusm.programs.model.TraceabilityRequest;
import org.springframework.stereotype.Service;

@Service
public interface ITraceabilityService {

    TraceabilityRequest<Object> getTraceabilityByProgramId(Integer programId);
    void createTraceability(TraceabilityRequest<FileModel> traceabilityRequest, Integer programId);
    void updateTraceability(TraceabilityRequest<FileModel> traceabilityRequest, Integer programId);

}
