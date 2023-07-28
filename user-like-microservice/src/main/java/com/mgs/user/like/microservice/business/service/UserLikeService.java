package com.mgs.user.like.microservice.business.service;

import com.mgs.library.model.MovieDTO;
import com.mgs.library.model.MovieLikeDTO;
import com.mgs.library.model.ReviewDTO;
import com.mgs.library.model.ReviewLikeDTO;
import com.mgs.library.model.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserLikeService {

    Optional<MovieLikeDTO> toggleMovieLike(MovieDTO movieDTO, UserDTO userDTO);
    Optional<ReviewLikeDTO> toggleReviewLike(ReviewDTO reviewDTO, UserDTO userDTO);

    List<MovieLikeDTO> getAllLikesForMovie(MovieDTO movieId);

    List<ReviewLikeDTO> getAllLikesForAReview(ReviewDTO review);

}
