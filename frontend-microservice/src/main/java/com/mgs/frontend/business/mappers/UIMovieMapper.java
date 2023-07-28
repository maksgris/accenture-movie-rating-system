package com.mgs.frontend.business.mappers;

import com.mgs.frontend.model.UIMovie;
import com.mgs.library.model.MovieDTO;
import com.mgs.library.model.MovieLikeDTO;
import com.mgs.library.model.ReviewDTO;
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
