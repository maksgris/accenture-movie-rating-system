package com.mgs.movietype.microservice.business.repository;

import com.mgs.library.business.repository.model.MovieType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MovieTypeRepository extends JpaRepository<MovieType, Long> {
    boolean
    existsByType(String type);

    Optional<MovieType> findMovieTypeByType(String type);
}
