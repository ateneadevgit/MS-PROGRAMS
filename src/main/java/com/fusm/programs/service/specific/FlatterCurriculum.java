package com.fusm.programs.service.specific;

import com.fusm.programs.model.external.CurriculumModel;

import java.util.ArrayList;
import java.util.List;

public class FlatterCurriculum {

    public List<CurriculumModel> flattenCurriculumModel(List<CurriculumModel> curriculumList) {
        List<CurriculumModel> flattenedList = new ArrayList<>();
        for (CurriculumModel curriculum : curriculumList) {
            flattenedList.add(curriculum);
            List<CurriculumModel> children = curriculum.getChilds();
            if (children != null && !children.isEmpty()) {
                flattenedList.addAll(flattenCurriculumModel(children));
            }
        }
        return flattenedList;
    }

}
