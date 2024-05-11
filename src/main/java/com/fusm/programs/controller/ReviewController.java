package com.fusm.programs.controller;

import com.fusm.programs.model.GetReviewModel;
import com.fusm.programs.model.ReviewModel;
import com.fusm.programs.model.ReviewRequest;
import com.fusm.programs.service.IReviewService;
import com.fusm.programs.util.AppRoutes;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Clase que expone los servicios relacionados con los comentarios de la edición de un programa académico
 * ITSense Inc - Andrea Gómez
 */

@RestController
@RequestMapping(value = AppRoutes.PROGRAMS_ROUTE + "/review")
public class ReviewController {

    @Autowired
    private IReviewService reviewService;


    /**
     * Crea un nuevo comentario dentro del módulo
     * @param reviewRequest Modelo que contiene la información necesaria para crear un comentario
     * @return OK
     */
    @PostMapping
    public ResponseEntity<String> createReview(
            @RequestBody ReviewRequest reviewRequest
            ) {
        reviewService.createReview(reviewRequest);
        return ResponseEntity.ok(HttpStatus.OK.getReasonPhrase());
    }

    /**
     * Obtiene los comentarios realizados sobre un módulo
     * @param getReviewModel Modelo que contiene la información para filtrar la consulta
     * @return lsita de comentarios
     */
    @PostMapping("/get")
    public ResponseEntity<List<ReviewModel>> getReviews(
            @RequestBody GetReviewModel getReviewModel
            ) {
        return ResponseEntity.ok(reviewService.getReviews(getReviewModel));
    }

}
