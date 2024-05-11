package com.fusm.programs.service.impl;

import com.fusm.programs.entity.SecondLanguage;
import com.fusm.programs.model.SecondLanguageModel;
import com.fusm.programs.model.SecondLanguageRequest;
import com.fusm.programs.repository.ISecondLanguageRepository;
import com.fusm.programs.service.ISecondLanguageService;
import com.fusm.programs.util.SharedMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class SecondLanguageService implements ISecondLanguageService {

    @Autowired
    private ISecondLanguageRepository secondLanguageRepository;

    @Autowired
    private SharedMethod sharedMethod;


    @Override
    public void createSecondLanguage(SecondLanguageRequest secondLanguageRequest) {
        secondLanguageRepository.save(
                SecondLanguage.builder()
                        .cover(sharedMethod.saveFile(secondLanguageRequest.getCover(), secondLanguageRequest.getCreatedBy()))
                        .tittle(secondLanguageRequest.getTittle())
                        .startLevel(secondLanguageRequest.getStartLevel())
                        .endLevel(secondLanguageRequest.getEndLevel())
                        .groupId(secondLanguageRequest.getGroupId())
                        .icon(sharedMethod.saveFile(secondLanguageRequest.getIcon(), secondLanguageRequest.getCreatedBy()))
                        .description(secondLanguageRequest.getDescription())
                        .modalityId(secondLanguageRequest.getModalityId())
                        .duration(secondLanguageRequest.getDuration())
                        .hours(secondLanguageRequest.getHours())
                        .inscriptionLink(secondLanguageRequest.getInscriptionLink())
                        .createdBy(secondLanguageRequest.getCreatedBy())
                        .createdAt(new Date())
                        .enabled(true)
                        .build()
        );
    }

    @Override
    public void updateSecondLanguage(SecondLanguageRequest secondLanguageRequest, Integer secondLanguageId) {
        Optional<SecondLanguage> secondLanguageOptional = secondLanguageRepository.findById(secondLanguageId);
        if (secondLanguageOptional.isPresent()) {
            SecondLanguage secondLanguage = secondLanguageOptional.get();
            if (secondLanguageRequest.getCover() != null) secondLanguage.setCover(sharedMethod.saveFile(secondLanguageRequest.getCover(), secondLanguageRequest.getCreatedBy()));
            secondLanguage.setTittle(secondLanguageRequest.getTittle());
            secondLanguage.setStartLevel(secondLanguageRequest.getStartLevel());
            secondLanguage.setEndLevel(secondLanguageRequest.getEndLevel());
            secondLanguage.setGroupId(secondLanguageRequest.getGroupId());
            if (secondLanguageRequest.getIcon() != null) secondLanguage.setIcon(sharedMethod.saveFile(secondLanguageRequest.getIcon(), secondLanguageRequest.getCreatedBy()));
            secondLanguage.setDescription(secondLanguageRequest.getDescription());
            secondLanguage.setModalityId(secondLanguageRequest.getModalityId());
            secondLanguage.setDuration(secondLanguageRequest.getDuration());
            secondLanguage.setHours(secondLanguageRequest.getHours());
            secondLanguage.setInscriptionLink(secondLanguageRequest.getInscriptionLink());
            secondLanguage.setCreatedBy(secondLanguageRequest.getCreatedBy());
            secondLanguage.setUpdatedAt(new Date());
            secondLanguageRepository.save(secondLanguage);
        }
    }

    @Override
    public void disableSecondLanguage(Integer secondLanguageId) {
        Optional<SecondLanguage> secondLanguageOptional = secondLanguageRepository.findById(secondLanguageId);
        if (secondLanguageOptional.isPresent()) {
            SecondLanguage secondLanguage = secondLanguageOptional.get();
            secondLanguage.setEnabled(false);
            secondLanguageRepository.save(secondLanguage);
        }
    }

    @Override
    public List<SecondLanguage> getSecondLanguages() {
        return secondLanguageRepository.findAllOrdered();
    }

    @Override
    public List<SecondLanguageModel> getSecondLanguagesByGroup() {
        List<SecondLanguage> secondLanguages = secondLanguageRepository.findAllOrdered();
        List<SecondLanguageModel> secondLanguageModelList = new ArrayList<>();
        Map<Integer, List<SecondLanguage>> secondLanguageGroup = secondLanguages.stream()
                .collect(Collectors.groupingBy(SecondLanguage::getGroupId, Collectors.toList()));
        for (Integer key : secondLanguageGroup.keySet()) {
            secondLanguageModelList.add(new SecondLanguageModel(key, secondLanguageGroup.get(key)));
        }
        return secondLanguageModelList;
    }

    @Override
    public SecondLanguage getSecondLanguageById(Integer secondLanguageId) {
        SecondLanguage secondLanguage = new SecondLanguage();
        Optional<SecondLanguage> secondLanguageOptional = secondLanguageRepository
                .findBySecondLanguageIdAndEnabled(secondLanguageId, true);
        if (secondLanguageOptional.isPresent()) {
            secondLanguage = secondLanguageOptional.get();
        }
        return secondLanguage;
    }

}
