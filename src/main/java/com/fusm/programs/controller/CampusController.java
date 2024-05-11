package com.fusm.programs.controller;

import com.fusm.programs.service.ICampusService;
import com.fusm.programs.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Clase que expone los servicios relacionados con las sedes
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.PROGRAMS_ROUTE)
public class CampusController {

    @Autowired
    private ICampusService campusService;


    /**
     * Obtiene las sedes segun el ID del programa
     * @param programId ID del programa
     * @return Lista de sedes
     */
    @GetMapping("/{id}/campus")
    public ResponseEntity<List<Integer>> getCampusByProgram(
            @PathVariable("id") Integer programId
    ) {
        return ResponseEntity.ok(campusService.getCampusByProgram(programId));
    }

}
