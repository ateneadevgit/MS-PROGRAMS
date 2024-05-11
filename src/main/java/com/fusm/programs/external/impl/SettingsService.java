package com.fusm.programs.external.impl;

import com.fusm.programs.external.ISettingsService;
import com.fusm.programs.model.external.SettingRequest;
import com.fusm.programs.webclient.WebClientConnector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SettingsService implements ISettingsService {

    @Autowired
    private WebClientConnector webClientConnector;

    @Value("${ms-settings.complete-path}")
    private String SETTINGS_ROUTE;

    @Value("${ms-settings.path}")
    private String SETTINGS_SERVICE;

    @Override
    public String getSetting(SettingRequest settingRequest) {
        return webClientConnector.connectWebClient(SETTINGS_ROUTE)
                .post()
                .uri(SETTINGS_SERVICE)
                .bodyValue(settingRequest)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

}
