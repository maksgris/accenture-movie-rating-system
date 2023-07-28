package com.mgs.review.microservice.business.repository;

import com.mgs.library.business.repository.model.Movie;
import com.mgs.library.business.repository.model.Review;
import com.mgs.library.business.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findReviewByUserId(User user);

    List<Review> findReviewByMovieId(Movie movie);

    boolean existsByMovieIdAndUserId(Movie movieId, User userId);

    @Query("select count(c) from Movie p join p.movieLikes c where p = ?1")
    Long countChildrenByParent(Review review);

}
