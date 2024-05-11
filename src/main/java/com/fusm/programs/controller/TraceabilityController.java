package com.fusm.programs.controller;

import com.fusm.programs.model.FileModel;
import com.fusm.programs.model.TraceabilityRequest;
import com.fusm.programs.service.ITraceabilityService;
import com.fusm.programs.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Clase que expone los servicios relacionados con el último formulario de los pasos de la creación de un programa académico
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.TRACEABILITY_ROUTE)
public class TraceabilityController {

    @Autowired
    private ITraceabilityService traceabilityService;


    /**
     * Obtener la trazabilidad de un programa
     * @param programId ID del programa
     * @return trazabilidad del programa
     */
    @GetMapping("/program-id/{id}")
    public ResponseEntity<TraceabilityRequest<Object>> getProgramTraceability(
            @PathVariable("id") Integer programId
    ) {
        return ResponseEntity.ok(traceabilityService.getTraceabilityByProgramId(programId));
    }

    /**
     * Crear la trazabilidad de un programa académico
     * @param programId ID del programa
     * @param traceabilityRequest Modelo que contiene la información necesaria para crear la trazabilidad de un programa
     * @return OK
     */
    @PostMapping("/program-id/{id}")
    public ResponseEntity<String> createProgramTraceability(
            @PathVariable("id") Integer programId,
            @RequestBody TraceabilityRequest<FileModel> traceabilityRequest
    ) {
        traceabilityService.createTraceability(traceabilityRequest, programId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Actualiza la trazabilidad de un programa académico
     * @param programId ID del programa
     * @param traceabilityRequest Modelo que contiene la información necesaria para actualizar la trazabilidad de un programa
     * @return OK
     */
    @PutMapping("/program-id/{id}")
    public ResponseEntity<String> updateProgramTraceability(
            @PathVariable("id") Integer programId,
            @RequestBody TraceabilityRequest<FileModel> traceabilityRequest
    ) {
        traceabilityService.updateTraceability(traceabilityRequest, programId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}
