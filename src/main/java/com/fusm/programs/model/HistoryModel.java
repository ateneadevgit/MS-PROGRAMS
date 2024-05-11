package com.fusm.programs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryModel {

    private Integer historyId;
    private Double version;
    private String createdBy;
    private Date editionDate;
    private String value;
    private String minute;

}
