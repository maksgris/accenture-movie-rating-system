package com.mgs.movie.microservice.business.repository;

import com.mgs.library.business.repository.model.Movie;
import com.mgs.library.business.repository.model.MovieType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovieRepository extends JpaRepository<Movie, Long> {
    boolean existsByTitle(String title);

    @Query(value = "SELECT * FROM movie ORDER BY RAND() Limit 1", nativeQuery = true)
    Movie findRandomMovie();

    List<Movie> findMovieByMovieType(MovieType movieType);

    @Query("select count(c) from Movie p join p.movieLikes c where p = ?1")
    Long countChildrenByParent(Movie movie);
}
