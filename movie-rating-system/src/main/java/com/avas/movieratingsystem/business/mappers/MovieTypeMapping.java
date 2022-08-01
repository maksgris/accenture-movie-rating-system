package com.avas.movieratingsystem.business.mappers;

import com.avas.movieratingsystem.business.repository.model.MovieType;
import com.avas.movieratingsystem.business.repository.model.User;
import com.avas.movieratingsystem.model.MovieTypeDTO;
import com.avas.movieratingsystem.model.UserDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MovieTypeMapping {
    MovieTypeDTO mapMovieTypeToMovieTypeDto(MovieType movieEntity);
    MovieType mapMovieTypeDtoToMovieType(MovieTypeDTO movieDto);

    List<MovieTypeDTO> mapMovieTypeListToMovieTypeListDto(List<MovieType> movieEntities);


}
