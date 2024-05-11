package com.fusm.programs.service.specific;

import com.fusm.programs.dto.MinuteDto;
import com.fusm.programs.model.GuidelineModel;
import com.fusm.programs.model.external.CurriculumModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProgramAttachmentMapper {

    public List<GuidelineModel> mapToTree(List<MinuteDto> minuteDtoList, boolean hasSearchParams) {
        Map<Integer, GuidelineModel> minuteMap = new HashMap<>();
        List<GuidelineModel> rootNodes = new ArrayList<>();

        for (MinuteDto minute : minuteDtoList) {
            GuidelineModel guidelineModel = GuidelineModel.builder()
                    .programAttachmentId(minute.getIdProgramAttachment())
                    .name(minute.getName())
                    .fileUrl(minute.getFileUrl())
                    .fileType(minute.getFileType())
                    .createdBy(minute.getCreatedBy())
                    .createdAt(minute.getCreatedAt())
                    .updatedAt(minute.getUpdatedAt())
                    .attachmentFatherId(minute.getAttachmentFatherId())
                    .attachChilds(new ArrayList<>())
                    .presentationChilds(new ArrayList<>())
                    .build();
            minuteMap.put(minute.getIdProgramAttachment(), guidelineModel);
        }

        if (hasSearchParams) {
            rootNodes = new ArrayList<>(minuteMap.values());
        } else {
            for (MinuteDto minute : minuteDtoList) {
                Integer fatherId = minute.getAttachmentFatherId();
                GuidelineModel currentModel = minuteMap.get(minute.getIdProgramAttachment());

                if (fatherId != null) {
                    GuidelineModel parent = minuteMap.get(fatherId);
                    if (parent != null) {
                        if (currentModel.getFileType().equals(88)) parent.addAttachChild(currentModel);
                        if (currentModel.getFileType().equals(87)) parent.addPresentationChild(currentModel);
                    }
                } else {
                    rootNodes.add(currentModel);
                }
            }
        }

        return rootNodes;
    }

}
