package com.fusm.programs.controller;

import com.fusm.programs.model.GuidelineModel;
import com.fusm.programs.model.GuidelineRequest;
import com.fusm.programs.model.SearchModel;
import com.fusm.programs.service.IProgramAttachmentService;
import com.fusm.programs.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase que expone los servicios relacionados con los documentos del programa presentes en los lineamientos
 * institucionales de gestión curricular y comités
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.PROGRAMS_ROUTE + "/attachment")
public class ProgramAttachmentController {

    @Autowired
    private IProgramAttachmentService programAttachmentService;


    /**
     * Obntiene todos los documentos
     * @return lista de documentos
     */
    @GetMapping("/guideline")
    public ResponseEntity<List<GuidelineModel>> getAllGuideline() {
        return ResponseEntity.ok(programAttachmentService.getGuidelineAttachment());
    }

    /**
     * Obtiene lista de todos los documentos segun el tipo
     * @param type tipo de documento si es guideline o minute
     * @return lsita de documentos
     */
    @GetMapping("/guideline/by-type/{type}")
    public ResponseEntity<List<GuidelineModel>> getGuidelineAttachmentByType(
            @PathVariable("type") Integer type
    ) {
        return ResponseEntity.ok(programAttachmentService.getGuidelineAttachmentByType(type));
    }

    /**
     * Obtiene una lsita de todas las actas
     * @param searchModel Modelo que contiene los parámetros de búsqueda prara filtrar
     * @return lista de actas
     */
    @PostMapping("/minute")
    public ResponseEntity<List<GuidelineModel>> getAllMinute(
            @RequestBody SearchModel searchModel
            ) {
        return ResponseEntity.ok(programAttachmentService.getMinutes(searchModel));
    }

    /**
     * Crea un lineamiento institucional
     * @param guidelineRequest Modelo que contiene la información necesaria para crear un nuevo lineamiento institucional
     * @return OK
     */
    @PostMapping
    public ResponseEntity<String> createGuideline(
            @RequestBody GuidelineRequest guidelineRequest
            ) {
        programAttachmentService.createGuidelineAttachment(guidelineRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Actualiza un lineamiento institucional
     * @param guidelineRequest Modelo que contiene la información necesaria para actualizar un lineamiento isntitucional
     * @param guidelineId ID del lineamiento institucional
     * @return OK
     */
    @PutMapping("/{id}")
    public ResponseEntity<String> updateGuideline(
            @RequestBody GuidelineRequest guidelineRequest,
            @PathVariable("id") Integer guidelineId
    ) {
        programAttachmentService.updateGuidelineAttachment(guidelineRequest, guidelineId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Deshabilita un lineamiento institucional
     * @param guidelineId ID del lineamiento institucional
     * @return OK
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGuideline(
            @PathVariable("id") Integer guidelineId
    ) {
        programAttachmentService.disableGuidelineAttachment(guidelineId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Elimina de forma permanente un lineamiento institucional
     * @param guidelineId ID del linemiento institucional
     * @return OK
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePermanentlyGuideline(
            @PathVariable("id") Integer guidelineId
    ) {
        programAttachmentService.deleteGuidelineAttachment(guidelineId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Habilita o Deshabilita un lineamiento institucional
     * @param guidelineId ID del lineamiento institucional
     * @param enabled Parámetro que indica si se activa o desactiva el lineamiento institucional
     * @return OK
     */
    @DeleteMapping("/{id}/enable-disable/{enabled}")
    public ResponseEntity<String> enableDisableGuideline(
            @PathVariable("id") Integer guidelineId,
            @PathVariable("enabled") Boolean enabled
    ) {
        programAttachmentService.enableDisableGuideline(guidelineId, enabled);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene el linemiento institucional según su ID
     * @param guidelineId ID del lineamiento institucional
     * @return URL del lineamiento institucional
     */
    @GetMapping("/guideline/{id}")
    public ResponseEntity<String> getGuidelineAttachmentById(
            @PathVariable("id") Integer guidelineId
    ) {
        return ResponseEntity.ok(programAttachmentService.getGuidelineAttachmentById(guidelineId));
    }

}
