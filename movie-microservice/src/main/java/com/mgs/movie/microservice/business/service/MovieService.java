package com.mgs.movie.microservice.business.service;


import com.mgs.library.model.MovieDTO;
import com.mgs.library.model.MovieTypeDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MovieService {

    List<MovieDTO>  getTopTenMovies();
    MovieDTO getRandomMovie();
    List<MovieDTO> getMovieOfAGenre(MovieTypeDTO movieGenre);
    List<MovieDTO> getAllMovies();
    Optional<MovieDTO> findMovieById(Long id);
    void deleteMovieById(Long id);
    MovieDTO createMovie(MovieDTO newMovie);
    MovieDTO updateMovieById(MovieDTO modifyExistingMovie, Long id);

}
