package com.fusm.programs.controller;

import com.fusm.programs.entity.SecondLanguage;
import com.fusm.programs.model.SecondLanguageModel;
import com.fusm.programs.model.SecondLanguageRequest;
import com.fusm.programs.service.ISecondLanguageService;
import com.fusm.programs.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase que expone los servicios necesarios para el centro de aprendizaje de una segunda lengua
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(AppRoutes.PROGRAMS_ROUTE + "/second-language")
public class SecondLanguageController {

    @Autowired
    private ISecondLanguageService secondLanguageService;


    /**
     * Crea un nuevo nivel de segunda lengua
     * @param secondLanguageRequest Modelo que contiene la información necesaria para crear un nuevo nivel
     * @return OK
     */
    @PostMapping
    private ResponseEntity<String> createSecondLanguage(
            @RequestBody SecondLanguageRequest secondLanguageRequest
            ) {
        secondLanguageService.createSecondLanguage(secondLanguageRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Actualiza un nivel de segunda lengua
     * @param secondLanguageRequest Modelo que contiene la información necesaria para actualizar un nivel
     * @param secondLanguageId ID del nivel
     * @return OK
     */
    @PutMapping("/{id}")
    private ResponseEntity<String> updateSecondLanguage(
            @RequestBody SecondLanguageRequest secondLanguageRequest,
            @PathVariable("id") Integer secondLanguageId
    ) {
        secondLanguageService.updateSecondLanguage(secondLanguageRequest, secondLanguageId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Dehabilita un nivel
     * @param secondLanguageId ID del nivel
     * @return OK
     */
    @DeleteMapping("/{id}")
    private ResponseEntity<String> disableSecondLanguage(
            @PathVariable("id") Integer secondLanguageId
    ) {
        secondLanguageService.disableSecondLanguage(secondLanguageId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene una lista de los niveles
     * @return lista de niveles
     */
    @GetMapping
    private ResponseEntity<List<SecondLanguage>> getSecondLanguages() {
        return ResponseEntity.ok(secondLanguageService.getSecondLanguages());
    }

    /**
     * Obtuiene una lista de los niveles agrupados por los grupos
     * @return lista de niveles
     */
    @GetMapping("/group")
    private ResponseEntity<List<SecondLanguageModel>> getSecondLanguageByGroup() {
        return ResponseEntity.ok(secondLanguageService.getSecondLanguagesByGroup());
    }

    /**
     * Obtiene el nivel por su ID
     * @param secondLanguageId ID del nivel
     * @return nivel
     */
    @GetMapping("/{id}")
    private ResponseEntity<SecondLanguage> getSecondLanguageById(
            @PathVariable("id") Integer secondLanguageId
    ) {
        return ResponseEntity.ok(secondLanguageService.getSecondLanguageById(secondLanguageId));
    }

}
