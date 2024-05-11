package com.fusm.programs.controller;

import com.fusm.programs.model.ProgramModuleModel;
import com.fusm.programs.service.IProgramModuleService;
import com.fusm.programs.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Clase que expone los servicios relacionados con los módulos de un programa académico
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(AppRoutes.PROGRAMS_ROUTE + "/module")
public class ProgramModuleController {

    @Autowired
    private IProgramModuleService programModuleService;


    /**
     * Obtiene los módulos de un programa académico
     * @param isEnlarge TRUE o FALSE en caso que sea un programa de renovación o no
     * @return lista de módulos
     */
    @GetMapping
    public ResponseEntity<List<ProgramModuleModel>> getProgramModules(
            @RequestParam("isEnlarge") boolean isEnlarge
    ) {
        return ResponseEntity.ok(programModuleService.getProgramModules(isEnlarge));
    }

    /**
     * Lista de los módulos no editables de un programa académico
     * @param isEnlarge TRUE o FALSE en caso que sea un programa de renovación o no
     * @return lista de módulos
     */
    @GetMapping("/no-editable")
    public ResponseEntity<List<ProgramModuleModel>> getProgramModulesNoEditable(
            @RequestParam("isEnlarge") boolean isEnlarge
    ) {
        return ResponseEntity.ok(programModuleService.getAllProgramModules(isEnlarge));
    }

}
