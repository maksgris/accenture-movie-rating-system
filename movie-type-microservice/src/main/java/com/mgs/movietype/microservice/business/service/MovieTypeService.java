package com.mgs.movietype.microservice.business.service;

import com.mgs.library.model.MovieTypeDTO;

import java.util.List;
import java.util.Optional;

public interface MovieTypeService {

    Optional<MovieTypeDTO> getMovieTypeByName(String movieTypeName);

    List<MovieTypeDTO> getAllMovieTypes();

    Optional<MovieTypeDTO> findMovieTypeById(Long id);

    void deleteMovieTypeById(Long id);

    MovieTypeDTO createMovieType(MovieTypeDTO newMovieType);

    MovieTypeDTO updateMovieTypeById(MovieTypeDTO modifyExistingMovieType, Long id);
}
