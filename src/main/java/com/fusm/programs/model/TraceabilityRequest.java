package com.fusm.programs.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraceabilityRequest<T> {

    private Date proposalAprovedDate;
    private MinuteRequest superiorCouncilMinute;
    private Date menEndDate;
    private MinuteRequest viceAcademicMinute;
    private Date nsacesDate;
    private String sinies;
    private MinuteRequest academicCouncilMinute;
    private List<CampusRequest> campus;
    private String createdBy;
    private Integer roleId;
    private T approvedMinute;

}
