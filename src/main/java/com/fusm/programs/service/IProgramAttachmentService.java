package com.fusm.programs.service;

import com.fusm.programs.dto.MinuteDto;
import com.fusm.programs.entity.ProgramAttachment;
import com.fusm.programs.model.GuidelineModel;
import com.fusm.programs.model.GuidelineRequest;
import com.fusm.programs.model.SearchModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IProgramAttachmentService {

    List<GuidelineModel> getGuidelineAttachment();
    List<GuidelineModel> getGuidelineAttachmentByType(Integer type);
    void createGuidelineAttachment(GuidelineRequest guidelineRequest);
    void updateGuidelineAttachment(GuidelineRequest guidelineRequest, Integer guidelineId);
    void disableGuidelineAttachment(Integer guidelineId);
    List<GuidelineModel> getMinutes(SearchModel searchModel);
    void deleteGuidelineAttachment(Integer guidelineId);
    void enableDisableGuideline(Integer guidelineId, Boolean enabled);
    String getGuidelineAttachmentById(Integer guidelineId);

}
