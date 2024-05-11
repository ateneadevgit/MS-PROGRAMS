package com.fusm.programs.util;

import com.fusm.programs.external.IDocumentManagerService;
import com.fusm.programs.external.ISettingsService;
import com.fusm.programs.model.FileModel;
import com.fusm.programs.model.external.DocumentRequest;
import com.fusm.programs.model.external.SettingRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class SharedMethod {

    @Autowired
    private IDocumentManagerService documentManagerService;

    @Autowired
    private ISettingsService settingsService;


    public Integer getSettingValue(String settingName) {
        return Integer.parseInt(
                settingsService.getSetting(
                        SettingRequest.builder()
                                .settingName(settingName)
                                .build()
                )
        );
    }

    public String getSettingValueOnString(String settingName) {
        return settingsService.getSetting(
                SettingRequest.builder()
                        .settingName(settingName)
                        .build()
        );
    }

    public String saveFile(FileModel fileModel, String userEmail) {
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