package com.fusm.programs.model;

import com.fusm.programs.model.external.CurriculumModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuidelineModel {

    private Integer programAttachmentId;
    private String name;
    private String fileUrl;
    private Integer fileType;
    private String createdBy;
    private Date createdAt;
    private Date updatedAt;
    private Boolean enabled;
    private Integer attachmentFatherId;
    private List<GuidelineModel> attachChilds;
    private List<GuidelineModel> presentationChilds;

    public void addAttachChild(GuidelineModel child) {
        attachChilds.add(child);
    }

    public void addPresentationChild(GuidelineModel child) {
        presentationChilds.add(child);
    }

}
