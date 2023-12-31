package com.mgs.user.like.microservice.business.repository;

import com.mgs.library.business.repository.model.Review;
import com.mgs.library.business.repository.model.ReviewLike;
import com.mgs.library.business.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserLikeRepository extends JpaRepository<ReviewLike, Long> {
    boolean existsByUserIdAndReviewId(User user, Review review);

    Optional<ReviewLike> findByUserIdAndReviewId(User user, Review review);

    List<ReviewLike> findAllByReviewId(Review review);

    List<ReviewLike> findAllByUserId(User user);
}
