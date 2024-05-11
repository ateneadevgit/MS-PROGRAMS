package com.fusm.programs.service;

import com.fusm.programs.entity.Campus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICampusService {

    List<Integer> getCampusByProgram(Integer program);
    void disableCampus(List<Integer> campusId, Integer programId);
}
