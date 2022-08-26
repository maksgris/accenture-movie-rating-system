package com.avas.review.microservice.business.repository;

import com.avas.review.microservice.business.repository.model.Movie;
import com.avas.review.microservice.business.repository.model.Review;
import com.avas.review.microservice.business.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findReviewByUserId(User user);
    boolean existsByMovieIdAndUserId(Movie movieId, User userId);

}