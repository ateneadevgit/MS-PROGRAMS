package com.fusm.programs.model.external;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SettingRequest {
    private String settingName;
}
