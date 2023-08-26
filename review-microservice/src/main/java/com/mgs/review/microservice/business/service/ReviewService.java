package com.mgs.review.microservice.business.service;

import com.mgs.library.model.ReviewDTO;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    List<ReviewDTO> getAllReviews();

    Optional<ReviewDTO> findReviewById(Long id);

    void deleteReviewById(Long id);

    ReviewDTO createReview(ReviewDTO newReview);

    ReviewDTO updateReviewById(ReviewDTO modifyExistingReview, Long id);

    List<ReviewDTO> getAllReviewsMadeByUserById(Long userId);

    List<ReviewDTO> getAllReviewsForAMovie(Long movieId);

    ReviewDTO getTopReviewForAMovie(Long movieId);
}
