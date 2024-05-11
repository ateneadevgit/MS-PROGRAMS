package com.fusm.programs.external;

import com.fusm.programs.model.external.SettingRequest;
import org.springframework.stereotype.Service;

@Service
public interface ISettingsService {
    String getSetting(SettingRequest settingRequest);
}