package com.fusm.programs.controller;

import com.fusm.programs.model.HistoryModel;
import com.fusm.programs.service.IHistoryService;
import com.fusm.programs.util.AppRoutes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Esta clase esta referenciada hacia el módulo del monitoreo curricular
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.PROGRAMS_ROUTE + "/history")
public class HistoryController {

    @Autowired
    private IHistoryService historyService;


    /**
     * Obtiene el histórico especifico de una entidad
     * @param programId ID del programa
     * @param moduleId ID del módulo
     * @return lista del histórico
     */
    @GetMapping("/program-id/{programId}/module-id/{moduleId}")
    public ResponseEntity<List<HistoryModel>> getHistorySpecific(
            @PathVariable("programId") Integer programId,
            @PathVariable("moduleId") Integer moduleId
    ) {
        return ResponseEntity.ok(historyService.getHistorySpecific(programId, moduleId));
    }

    /**
     * Obtener el historial por el tipo
     * @param programId ID del programa
     * @param moduleId ID del modulo
     * @param typeId ID del tipo
     * @return lista de historial
     */
    @GetMapping("/program-id/{programId}/module-id/{moduleId}/type/{typeId}")
    public ResponseEntity<List<HistoryModel>> getHistoryByType(
            @PathVariable("programId") Integer programId,
            @PathVariable("moduleId") Integer moduleId,
            @PathVariable("typeId") Integer typeId
    ) {
        return ResponseEntity.ok(historyService.gitHistoricByType(programId, moduleId, typeId));
    }

}
