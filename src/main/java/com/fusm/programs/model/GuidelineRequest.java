package com.fusm.programs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuidelineRequest {

    private FileModel file;
    private String fileName;
    private Integer fatherId;
    private Integer fileType;
    private Integer fileOrder;
    private String createdBy;

}
