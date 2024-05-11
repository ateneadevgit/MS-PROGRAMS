package com.fusm.programs.model;

import com.fusm.programs.dto.ProgramDto;
import lombok.*;

@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramModelImpl implements ProgramDto {

    private Integer idProgram;
    private String name;
    private String logo;
    private Integer idFaculty;
    private Integer idLevelFormation;
    private String fileUrl;
    private String createdAt;
    private Integer idStatus;
    private String campusList;
    private String developmentDate;
    private Integer idRegistryType;
    private String menEndDate;
    private Integer idTypeFormation;
    private Integer facultyId;

    @Override
    public Integer getIdProgram() {
        return this.idProgram;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getFacultyId() {
        return this.facultyId;
    }

    @Override
    public Integer getIdTypeFormation() {
        return this.idTypeFormation;
    }

}
