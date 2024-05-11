package com.fusm.programs.service;

import com.fusm.programs.entity.SecondLanguage;
import com.fusm.programs.model.SecondLanguageModel;
import com.fusm.programs.model.SecondLanguageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ISecondLanguageService {

    void createSecondLanguage(SecondLanguageRequest secondLanguageRequest);
    void updateSecondLanguage(SecondLanguageRequest secondLanguageRequest, Integer secondLanguageId);
    void disableSecondLanguage(Integer secondLanguageId);
    List<SecondLanguage> getSecondLanguages();
    List<SecondLanguageModel> getSecondLanguagesByGroup();
    SecondLanguage getSecondLanguageById(Integer secondLanguageId);

}
