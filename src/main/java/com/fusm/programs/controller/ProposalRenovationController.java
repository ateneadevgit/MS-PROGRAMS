package com.fusm.programs.controller;

import com.fusm.programs.model.*;
import com.fusm.programs.service.IProposalRenovationService;
import com.fusm.programs.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase que expone los servicios necesarios para la edición de un programa académico
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.PROGRAMS_ROUTE + "/upgrade")
public class ProposalRenovationController {

    @Autowired
    private IProposalRenovationService proposalRenovationService;


    /**
     * Crea una propuesta de renovación
     * @param programId ID del programa
     * @param proposalUpgradeRequest Modelo que contiene la información de la propuesta de renovación a crear
     * @return OK
     */
    @PostMapping("/{id}")
    public ResponseEntity<String> createProposalRenovation(
            @PathVariable("id") Integer programId,
            @RequestBody ProposalUpgradeRequest proposalUpgradeRequest
            ) {
        proposalRenovationService.createProposalRenovation(proposalUpgradeRequest, programId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Evalúa la propuesta de renovación
     * @param programId ID del programa
     * @param evaluateProposalRenovationRequest Modelo que contiene la información necesaria para evaluar la propuesta de renovación
     * @return OK
     */
    @PostMapping("/evaluate/{id}")
    public ResponseEntity<String> evaluateProposalRenovation(
            @PathVariable("id") Integer programId,
            @RequestBody EvaluateProposalRenovationRequest evaluateProposalRenovationRequest
            ) {
        proposalRenovationService.evaluateProposalRenovation(evaluateProposalRenovationRequest, programId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene los modulos seleccionados para la edición
     * @param programId ID del programa
     * @return lista de módulos
     */
    @GetMapping("/evaluate/{id}")
    public ResponseEntity<List<ProgramModuleModel>> getSelectedModules(
            @PathVariable("id") Integer programId
    ) {
        return ResponseEntity.ok(proposalRenovationService.getSelectedModules(programId));
    }

    /**
     * Obtiene una lista de los módulos que se encuentran en edición
     * @param programId ID del programa
     * @return lista de módulos
     */
    @GetMapping("/edition/{id}")
    public ResponseEntity<OnEditionModules> getModulesOnEdition(
        @PathVariable("id") Integer programId
    ) {
        return ResponseEntity.ok(proposalRenovationService.getOnEditionModules(programId));
    }

    /**
     * Obtiene la propuesta de edición de un programa académico
     * @param programId ID del programa
     * @return renovación
     */
    @GetMapping("/program-id/{id}")
    private ResponseEntity<RenovationModel> getProposalRenovation(
            @PathVariable("id") Integer programId
    ) {
        return ResponseEntity.ok(proposalRenovationService.getRequestProposal(programId));
    }

}
