package com.mgs.library.business.mappers;

import com.mgs.library.business.repository.model.Movie;
import com.mgs.library.business.repository.model.MovieType;
import com.mgs.library.model.MovieTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MovieTypeMapping {

    @Mapping(source = "movies", target = "movieIds", qualifiedByName = "moviesToMoviesIdLong")
    MovieTypeDTO mapMovieTypeToMovieTypeDto(MovieType movieEntity);

    @Mapping(source = "movieIds", target = "movies", qualifiedByName = "movieIdLongToMovie")
    MovieType mapMovieTypeDtoToMovieType(MovieTypeDTO movieDto);

    List<MovieTypeDTO> mapMovieTypeListToMovieTypeListDto(List<MovieType> movieEntities);

    List<MovieType> mapMovieTypeListDtoToMovieTypeList(List<MovieTypeDTO> movieEntities);

    @Named("moviesToMoviesIdLong")
    default List<Long> moviesToMoviesIdLong(List<Movie> listOfMovies) {
        return listOfMovies.stream()
                .map(Movie::getId).collect(Collectors.toList());
    }

    @Named("movieIdLongToMovie")
    default List<Movie> movieIdLongToMovie(List<Long> listOfMovieIds) {
        if (listOfMovieIds == null)
            return new ArrayList<>();
        return listOfMovieIds.stream()
                .map(Movie::new).collect(Collectors.toList());
    }

}
