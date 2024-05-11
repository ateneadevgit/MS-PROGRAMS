package com.fusm.programs.service.specific;

import com.fusm.programs.model.external.CurriculumModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CurriculumMapper {

    public List<CurriculumModel> mapToTree(List<CurriculumModel> curriculumModels) {
        Map<Integer, CurriculumModel> curriculumMap = new HashMap<>();
        List<CurriculumModel> rootNodes = new ArrayList<>();

        for (CurriculumModel curriculum : curriculumModels) {
            curriculumMap.put(curriculum.getCurriculumId(), curriculum);
        }

        for (CurriculumModel curriculum : curriculumModels) {
            Integer fatherId = curriculum.getFatherId();
            if (fatherId != null) {
                CurriculumModel parent = curriculumMap.get(fatherId);
                if (parent != null) {
                    parent.addChild(curriculum);
                }
            } else {
                rootNodes.add(curriculum);
            }
        }

        return rootNodes;
    }

}