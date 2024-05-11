package com.fusm.programs.service.impl;

import com.fusm.programs.entity.Program;
import com.fusm.programs.entity.ProposalRenovation;
import com.fusm.programs.entity.ProposalRenovationModules;
import com.fusm.programs.entity.Review;
import com.fusm.programs.model.GetReviewModel;
import com.fusm.programs.model.ReviewModel;
import com.fusm.programs.model.ReviewRequest;
import com.fusm.programs.repository.IProgramRepository;
import com.fusm.programs.repository.IProposalRenovationModuleRepository;
import com.fusm.programs.repository.IProposalRenovationRepository;
import com.fusm.programs.repository.IReviewRepository;
import com.fusm.programs.service.IReviewService;
import com.fusm.programs.util.Constant;
import com.fusm.programs.util.SharedMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ReviewService implements IReviewService {

    @Autowired
    private IReviewRepository reviewRepository;

    @Autowired
    private SharedMethod sharedMethod;

    @Autowired
    private IProposalRenovationRepository proposalRenovationRepository;

    @Autowired
    private IProgramRepository programRepository;

    @Autowired
    private IProposalRenovationModuleRepository proposalRenovationModuleRepository;


    @Override
    public void createReview(ReviewRequest reviewRequest) {
        Review reviewFather = null;

        if (reviewRequest.getReplyTo() != null) {
            Optional<Review> fatherOptional = reviewRepository.findById(reviewRequest.getReplyTo());
            if (fatherOptional.isPresent()) reviewFather = fatherOptional.get();
        }

        Integer objectData = getObjectId(reviewRequest.getObjectType(), reviewRequest.getObjectId(), reviewRequest.getObjectComplementaryId());
        int objectId = (objectData != null) ? objectData : reviewRequest.getObjectId();

        reviewRepository.save(
                Review.builder()
                        .review(reviewRequest.getReview())
                        .objectType(reviewRequest.getObjectType())
                        .objectId(objectId)
                        .createdAt(new Date())
                        .createdBy(reviewRequest.getCreatedBy())
                        .roleId(reviewRequest.getRoleId())
                        .replyTo(reviewFather)
                        .build()
        );
    }

    @Override
    public List<ReviewModel> getReviews(GetReviewModel getReviewModel) {
        Integer objectData = getObjectId(getReviewModel.getType(), getReviewModel.getObjectId(), getReviewModel.getComplementaryId());
        int objectId = (objectData != null) ? objectData : getReviewModel.getObjectId();
        List<Review> reviewList = reviewRepository.findAllByObjectIdAndObjectType(objectId, getReviewModel.getType());
        return mapToReviewModel(reviewList);
    }

    private Integer getObjectId(Integer type, Integer id, Integer complementaryId) {
        Integer objectId = null;
        if (type.equals(sharedMethod.getSettingValue(Constant.REVIEW_UPGRADE_PROGRAM_TYPE))) {
            List<ProposalRenovation> proposalRenovationList = proposalRenovationRepository.findLastUpgradeRequest(id);
            Optional<Program> programOptional = programRepository.findById(id);

            if (!proposalRenovationList.isEmpty() && programOptional.isPresent()) {
                ProposalRenovation proposalRenovation = proposalRenovationList.get(0);
                List<ProposalRenovationModules> proposalRenovationModules = proposalRenovationModuleRepository
                        .findAllByProposalRenovationId_ProposalRenovationIdAndProgramModuleId_ProgramModuleId(proposalRenovation.getProposalRenovationId(), complementaryId);
                if (!proposalRenovationModules.isEmpty()) objectId = proposalRenovationModules.get(0).getRenovationModuleId();
            }
        }
        return objectId;
    }

    private List<ReviewModel> mapToReviewModel(List<Review> reviewList) {
        Map<Integer, ReviewModel> nodeMap = new HashMap<>();

        for (Review review : reviewList) {
            ReviewModel node = ReviewModel.builder()
                    .reviewId(review.getReviewId())
                    .review(review.getReview())
                    .roleId(review.getRoleId())
                    .createdBy(review.getCreatedBy())
                    .createdAt(review.getCreatedAt())
                    .replies(new ArrayList<>())
                    .build();
            nodeMap.put(review.getReviewId(), node);
        }

        for (Review review : reviewList) {
            ReviewModel currentNode = nodeMap.get(review.getReviewId());
            Review parent = review.getReplyTo();
            if (parent != null) {
                ReviewModel parentNode = nodeMap.get(parent.getReviewId());
                if (parentNode != null) {
                    parentNode.addRepliedTo(currentNode);
                }
            }
        }

        List<ReviewModel> roots = new ArrayList<>();
        for (Review review : reviewList) {
            ReviewModel currentNode = nodeMap.get(review.getReviewId());
            Review parent = review.getReplyTo();
            if (parent == null) {
                roots.add(currentNode);
            }
        }

        return roots;
    }

}
