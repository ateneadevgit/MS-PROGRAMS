package com.fusm.programs.service;

import com.fusm.programs.model.HistoryModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IHistoryService {

    List<HistoryModel> getHistorySpecific(Integer programId, Integer moduleId);
    List<HistoryModel> gitHistoricByType(Integer programId, Integer moduleId, Integer typeId);
    void createHistoric(String value, Integer moduleId, Integer programId, String createdBy, Integer type);

}
