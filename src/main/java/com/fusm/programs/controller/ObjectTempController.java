package com.fusm.programs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fusm.programs.model.EvaluateObjectRequest;
import com.fusm.programs.model.ObjectTempModel;
import com.fusm.programs.model.ObjectTempRequest;
import com.fusm.programs.model.ObjectToReview;
import com.fusm.programs.model.external.CoreAndSubcoreModel;
import com.fusm.programs.service.IObjectTempService;
import com.fusm.programs.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase que expone los sevicios relacionados con los objetos temporales aplicados en el módulo de ciclo curricular del programa académico o edición de un programa
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.PROGRAMS_ROUTE + "/object")
public class ObjectTempController {

    @Autowired
    private IObjectTempService objectTempService;


    /**
     * Crea un objeto temporal
     * @param programId ID del programa
     * @param objectTempRequest Modelo que contiene la información necesaria para crear un objeto temporal
     * @return OK
     * @throws JsonProcessingException
     */
    @PostMapping("/program/{id}")
    public ResponseEntity<String> createdObjectTemp(
            @PathVariable("id") Integer programId,
            @RequestBody ObjectTempRequest objectTempRequest
            ) throws JsonProcessingException {
        objectTempService.createdObjectTemp(objectTempRequest, programId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Evalúa un objeto temporal
     * @param programId ID del programa
     * @param evaluateObjectRequest Modelo que contiene la información del objeto a evaluar
     * @return OK
     * @throws JsonProcessingException
     */
    @PostMapping("/evaluate/program/{id}")
    public ResponseEntity<String> evaluateObject(
            @PathVariable("id") Integer programId,
            @RequestBody EvaluateObjectRequest evaluateObjectRequest
    ) throws JsonProcessingException {
        objectTempService.evaluateObject(evaluateObjectRequest, programId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene la información de un objeto temporal
     * @param moduleId ID del módulo
     * @param programId ID del programa
     * @return objeto temporal
     */
    @GetMapping("/module-id/{moduleId}/program-id/{programId}")
    public ResponseEntity<ObjectTempModel> getObjectTemp(
            @PathVariable("moduleId") Integer moduleId,
            @PathVariable("programId") Integer programId
    ) {
        return ResponseEntity.ok(objectTempService.getObjectTemp(programId, moduleId));
    }

    /**
     * Actualiza un objeto temporal
     * @param programId ID del programa
     * @param objectTempRequest Modelo que contiene la información para actualizar un objeto temporal
     * @return OK
     * @throws JsonProcessingException
     */
    @PutMapping("/program-id/{programId}")
    public ResponseEntity<String> updateObjectTemp(
            @PathVariable("programId") Integer programId,
            @RequestBody ObjectTempRequest objectTempRequest
    ) throws JsonProcessingException {
        objectTempService.updateObjectTemp(objectTempRequest, programId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene la cantidad de créditos del objeto temporal
     * @param moduleId ID del módulo
     * @param programId ID del programa
     * @return número de créditos
     * @throws JsonProcessingException
     */
    @GetMapping("/module-id/{moduleId}/program-id/{programId}/credits")
    public ResponseEntity<Integer> getObjectNumberCredits(
            @PathVariable("moduleId") Integer moduleId,
            @PathVariable("programId") Integer programId
    ) throws JsonProcessingException {
        return ResponseEntity.ok(objectTempService.getObjectNumberCredits(moduleId, programId));
    }

    /**
     * Obtiene los núcleos de un objeto temporal
     * @param moduleId ID del módulo
     * @param programId ID del programa
     * @return lista de núcleos y subnúcleos
     * @throws JsonProcessingException
     */
    @GetMapping("/module-id/{moduleId}/program-id/{programId}/core")
    public ResponseEntity<List<CoreAndSubcoreModel>> getCores(
            @PathVariable("moduleId") Integer moduleId,
            @PathVariable("programId") Integer programId
    ) throws JsonProcessingException {
        return ResponseEntity.ok(objectTempService.getCores(moduleId, programId));
    }

    /**
     * Obtiene las asignaturas según un núcleo dentro de un objeto temporal
     * @param moduleId ID del módulo
     * @param programId ID del programa
     * @param coreId ID del núcleo
     * @return lista de núcleos y subnúcleos
     * @throws JsonProcessingException
     */
    @GetMapping("/module-id/{moduleId}/program-id/{programId}/by-core/{coreId}")
    public ResponseEntity<List<CoreAndSubcoreModel>> getSubjectsByCore(
            @PathVariable("moduleId") Integer moduleId,
            @PathVariable("programId") Integer programId,
            @PathVariable("coreId") Integer coreId
    ) throws JsonProcessingException {
        return ResponseEntity.ok(objectTempService.getSubjectsByCore(coreId, moduleId, programId));
    }

    /**
     * Envía el módulo a revisión
     * @param objectToReview Modelo que contiene la información del módulo que se enviará a revisión
     * @return OK
     * @throws JsonProcessingException
     */
    @PostMapping("/send-review")
    public ResponseEntity<String> sendModuleToReview(
            @RequestBody ObjectToReview objectToReview
            ) throws JsonProcessingException {
        objectTempService.sendObjectToReview(objectToReview);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

}
