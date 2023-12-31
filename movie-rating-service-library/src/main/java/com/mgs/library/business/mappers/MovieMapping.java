package com.mgs.library.business.mappers;

import com.mgs.library.business.repository.model.Movie;
import com.mgs.library.business.repository.model.MovieType;
import com.mgs.library.business.repository.model.Review;
import com.mgs.library.model.MovieDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface MovieMapping {

    @Mapping(source = "reviewIds", target = "reviewIds", qualifiedByName = "reviewIdsToReviewIdsLong")
    @Mapping(source = "movieType", target = "movieType", qualifiedByName = "movieTypeToMovieTypeString")
    MovieDTO mapMovieToMovieDto(Movie movieEntity);

    @Mapping(source = "reviewIds", target = "reviewIds", qualifiedByName = "reviewIdsLongToReviewIds")
    @Mapping(source = "movieType", target = "movieType", qualifiedByName = "movieTypeStringToMovieType")
    Movie mapMovieDtoToMovie(MovieDTO movieDto);

    List<MovieDTO> mapMovieListToMovieListDto(List<Movie> movieEntities);

    List<Movie> mapMovieDtoListToMovieList(List<MovieDTO> movieEntities);

    @Named("reviewIdsToReviewIdsLong")
    default List<Long> reviewIdsToReviewIdsLong(List<Review> listOfReviewIds) {
        return listOfReviewIds.stream()
                .map(Review::getId).collect(Collectors.toList());
    }

    @Named("reviewIdsLongToReviewIds")
    default List<Review> reviewIdsLongToReviewIds(List<Long> listOfReviewIdsLong) {
        return listOfReviewIdsLong.stream()
                .map(Review::new).collect(Collectors.toList());
    }

    @Named("movieTypeToMovieTypeString")
    default String movieTypeStringToMovieType(MovieType movieType) {
        return movieType.getType();
    }

    @Named("movieTypeStringToMovieType")
    default MovieType movieTypeStringToMovieType(String movieType) {
        return new MovieType(movieType);
    }

}
