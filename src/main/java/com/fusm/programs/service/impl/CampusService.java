package com.fusm.programs.service.impl;

import com.fusm.programs.entity.Campus;
import com.fusm.programs.repository.ICampusRepository;
import com.fusm.programs.service.ICampusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CampusService implements ICampusService {

    @Autowired
    private ICampusRepository campusRepository;


    @Override
    public List<Integer> getCampusByProgram(Integer programId) {
        return campusRepository.findAllByProgramId_ProgramIdAndEnabled(programId, true).stream().map(
                Campus::getValue
        ).toList();
    }

    @Override
    public void disableCampus(List<Integer> campusId, Integer programId) {
        List<Campus> campusList = campusRepository.findCampusNotIncluded(campusId.toArray(new Integer[0]), programId);
        for (Campus campus : campusList) {
            campus.setEnabled(false);
            campusRepository.save(campus);
        }
    }

}
