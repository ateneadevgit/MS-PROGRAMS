package com.fusm.programs.model;

import com.fusm.programs.entity.SecondLanguage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SecondLanguageModel {

    private Integer groupId;
    private List<SecondLanguage> secondLanguages;

}
