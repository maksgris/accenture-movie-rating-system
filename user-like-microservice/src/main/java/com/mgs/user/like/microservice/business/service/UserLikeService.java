package com.mgs.user.like.microservice.business.service;

import com.mgs.library.model.ReviewLikeDTO;

import java.util.List;
import java.util.Optional;

public interface UserLikeService {

    List<ReviewLikeDTO> getAllUserLikes(Long userId);

    Optional<ReviewLikeDTO> toggleReviewLike(Long reviewId, Long reviewerUserId);

    List<ReviewLikeDTO> getAllLikesForAReview(Long reviewId);

}
