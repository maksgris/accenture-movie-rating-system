package com.avas.frontend.business.mappers;

import com.avas.frontend.model.UIMovie;
import com.avas.library.model.MovieDTO;
import com.avas.library.model.MovieLikeDTO;
import com.avas.library.model.ReviewDTO;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UIMovieMapper {


    public UIMovie mapToUIMovie(MovieDTO movie,
                                ReviewDTO review,
                                List<MovieLikeDTO> userLikes) {
        if (movie == null) return null;
        UIMovie uiMovie = new UIMovie();
        uiMovie.setId(movie.getId());
        uiMovie.setTitle(movie.getTitle());
        uiMovie.setDescription(movie.getDescription());
        uiMovie.setMovieType(movie.getMovieType());
        uiMovie.setTopReviewText(Optional.ofNullable(review).map(ReviewDTO::getTextReview).orElse(null));
        uiMovie.setScore(Optional.ofNullable(review).map(ReviewDTO::getScore).orElse(null));
        uiMovie.setLikes(Optional.ofNullable(userLikes).map(List::size).orElse(0));
        return uiMovie;
    }
}
