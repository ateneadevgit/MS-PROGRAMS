package com.fusm.programs.service;

import com.fusm.programs.model.GetReviewModel;
import com.fusm.programs.model.ReviewModel;
import com.fusm.programs.model.ReviewRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IReviewService {

    void createReview(ReviewRequest reviewRequest);
    List<ReviewModel> getReviews(GetReviewModel getReviewModel);

}
