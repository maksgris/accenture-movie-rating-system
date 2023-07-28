package com.mgs.user.like.microservice.business.repository;

import com.mgs.library.business.repository.model.Movie;
import com.mgs.library.business.repository.model.MovieLike;
import com.mgs.library.business.repository.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieLikeRepository extends JpaRepository<MovieLike, Long> {
    List<MovieLike> findMovieLikeByMovieId(Movie movie);
    Optional<MovieLike> findByMovieIdAndUserId(Movie movie, User user);
    boolean existsByMovieIdAndUserId(Movie movie, User user);
}
