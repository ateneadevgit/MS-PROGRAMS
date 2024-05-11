package com.fusm.programs.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fusm.programs.dto.*;
import com.fusm.programs.model.*;
import com.fusm.programs.service.IProgramService;
import com.fusm.programs.service.impl.ProgramService;
import com.fusm.programs.util.AppRoutes;
import com.fusm.programs.util.SharedMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Clase que expone los servicios necesarios relacionados con los programas
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.PROGRAMS_ROUTE)
public class ProgramController {

    @Autowired
    private IProgramService programService;


    /**
     * Obtiene los programas según la facultad
     * @param facultyId ID de la facultad
     * @return lista de programas
     */
    @GetMapping("/faculty-id/{id}")
    private ResponseEntity<List<ProgramDto>> getProgram(
            @PathVariable("id") Integer facultyId
    ) {
        return ResponseEntity.ok(programService.getPrograms(facultyId));
    }

    /**
     * Obtiene los programas según una variedad de facultad
     * @param facultyIds Lista de facultades
     * @return Lista de programas
     */
    @PostMapping("/faculty")
    private ResponseEntity<List<ProgramDto>> getProgramByFaculties(
            @RequestBody List<Integer> facultyIds
    ) {
        return ResponseEntity.ok(programService.getProgramsByFaculty(facultyIds));
    }

    /**
     * Obtiene los programas técnicos y tecnológicos según la facultad
     * @param facultyId ID de la facultas
     * @return lista de programas
     */
    @GetMapping("/technical/faculty-id/{id}")
    private ResponseEntity<List<TecnicalProgramDto>> getTecnicalProgram(
            @PathVariable("id") Integer facultyId
    ) {
        return ResponseEntity.ok(programService.getTecnicalPrograms(facultyId));
    }

    /**
     * Crea una propuesta de programa académico
     * @param programRequestModel Modelo que contiene la información necesaria para crear una propuesta de programa académico
     * @return OK
     */
    @PostMapping("/proposal")
    private ResponseEntity<String> createProposal(
            @RequestBody ProgramRequestModel programRequestModel
            ) {
        programService.createProposal(programRequestModel);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Actualiza la propuesta académica
     * @param programUpdateModel Modelo que contiene la información necesaria para actualizar una propuesta académica
     * @param proposalId ID de la propuesta
     * @return OK
     */
    @PutMapping("/proposal/{id}")
    private ResponseEntity<String> updateProposal(
            @RequestBody ProgramUpdateModel programUpdateModel,
            @PathVariable("id") Integer proposalId
    ) {
        programService.updateProposal(programUpdateModel, proposalId);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene las propuestas creadas por un usuario
     * @param params parámetros como page y size para paginar la respuesta y facultyId y campusId para filtrar el resultado
     * @param roleRequest contiene la información necesaria para filtrar la consulta
     * @return lista de propuestas académicas
     * @throws JsonProcessingException
     */
    @PostMapping("/get/proposal")
    private ResponseEntity<ResponsePage<String>> getProposal(
            @RequestParam Map<String, Object> params,
            @RequestBody RoleRequest roleRequest
    ) throws JsonProcessingException {
        int page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        int size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;
        Integer facultyId = params.get("facultyId") != null ? Integer.parseInt(params.get("facultyId").toString()) : null;
        Integer campusId = params.get("campusId") != null ? Integer.parseInt(params.get("campusId").toString()) : null;

        PageModel pageRequest = PageModel.builder()
                .size(size)
                .page(page)
                .build();

        QueryFilterProposal queryFilterProposal = QueryFilterProposal.builder()
                .campusId(campusId)
                .facultyId(facultyId)
                .build();

        return new ResponseEntity<>(
                programService.getProposal(queryFilterProposal, pageRequest, roleRequest),
                HttpStatus.OK
        );
    }

    /**
     * Lista de programas según su estado (activo, en construcción, desactivados, y declinados)
     * @param params parámetros como page y size para paginar la respuesta y facultyId y campusId para filtrar el resultado
     * @param roleRequest contiene la información necesaria para filtrar la consulta
     * @return lista de programas académicos
     * @throws JsonProcessingException
     */
    @PostMapping("/by-status")
    private ResponseEntity<ResponsePage<String>> getProgramByStatus(
            @RequestParam Map<String, Object> params,
            @RequestBody RoleRequest roleRequest
    ) throws JsonProcessingException {
        int page = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
        int size = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;
        Integer facultyId = params.get("facultyId") != null ? Integer.parseInt(params.get("facultyId").toString()) : null;
        Integer campusId = params.get("campusId") != null ? Integer.parseInt(params.get("campusId").toString()) : null;
        String programStatus = params.get("programStatus") != null ? params.get("programStatus").toString() : null;

        PageModel pageRequest = PageModel.builder()
                .size(size)
                .page(page)
                .build();

        QueryFilterProposal queryFilterProposal = QueryFilterProposal.builder()
                .campusId(campusId)
                .facultyId(facultyId)
                .build();

        return new ResponseEntity<>(
                programService.getProgramsByStatus(queryFilterProposal, pageRequest, programStatus, roleRequest),
                HttpStatus.OK
        );
    }

    /**
     * Obtiene el programa por su ID
     * @param programId ID del programa
     * @return programa
     */
    @GetMapping("/{id}")
    private ResponseEntity<ProgramIdDto> getProgramById(
            @PathVariable("id") Integer programId
    ) {
        return ResponseEntity.ok(programService.getProgramById(programId));
    }

    /**
     * Obtiene el histórico de estados de una propuesta
     * @param proposalId ID propuesta
     * @return lista de historial
     */
    @GetMapping("/historic/{id}")
    private ResponseEntity<List<HistoricDto>> getProposalHistoric(
            @PathVariable("id") Integer proposalId
    ) {
        return ResponseEntity.ok(programService.getFeedbackProposalHistoric(proposalId));
    }

    /**
     * Obtiene si el programa es una renovación o no
     * @param programId ID del programa
     * @return TRUE o FALSE si el programa es una renovación o no
     */
    @GetMapping("/{id}/is-formal")
    private ResponseEntity<Boolean> getIsEnlarge(
            @PathVariable("id") Integer programId
    ) {
        return ResponseEntity.ok(programService.getIsFormal(programId));
    }

    /**
     * Actualiza el estado de un programa
     * @param programId ID del programa
     * @return OK
     */
    @GetMapping("/{id}/upgrade-status")
    private ResponseEntity<Integer> getUpgradeProgramStatus(
            @PathVariable("id") Integer programId
    ) {
        return ResponseEntity.ok(programService.getUpdateStatus(programId));
    }

    /**
     * Obtiene la fecha en que se aprovó la propuesta
     * @param programId ID del programa
     * @return DATE
     */
    @GetMapping("/{id}/approved-date")
    private ResponseEntity<Date> getApprovedProposalDate(
            @PathVariable("id") Integer programId
    ) {
        return ResponseEntity.ok(programService.getApprovedProposalDate(programId));
    }

    /**
     * Obtiene los programas académicos
     * @param queryFilterProposal Modelo que contiene los parámetros de búsqueda para realizar filtros
     * @return lista de programas académicos
     */
    @PostMapping("/own")
    private ResponseEntity<List<OwnProgramDto>> getProgramAcademic(
            @RequestBody QueryFilterProposal queryFilterProposal
    ) {
        return ResponseEntity.ok(programService.getProgramAcademic(queryFilterProposal));
    }

}
