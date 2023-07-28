package com.mgs.user.like.microservice.business.service.impl;

import com.mgs.library.business.mappers.MovieLikeMapper;
import com.mgs.library.business.mappers.MovieMapping;
import com.mgs.library.business.mappers.ReviewMapping;
import com.mgs.library.business.mappers.UserLikeMapper;
import com.mgs.library.business.mappers.UserMapping;
import com.mgs.library.business.repository.model.Movie;
import com.mgs.library.business.repository.model.MovieLike;
import com.mgs.library.business.repository.model.Review;
import com.mgs.library.business.repository.model.ReviewLike;
import com.mgs.library.business.repository.model.User;
import com.mgs.library.model.MovieDTO;
import com.mgs.library.model.MovieLikeDTO;
import com.mgs.library.model.ReviewDTO;
import com.mgs.library.model.ReviewLikeDTO;
import com.mgs.library.model.UserDTO;
import com.mgs.user.like.microservice.business.repository.MovieLikeRepository;
import com.mgs.user.like.microservice.business.repository.UserLikeRepository;
import com.mgs.user.like.microservice.business.service.UserLikeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Log4j2
@Service
public class UserLikeServiceImpl implements UserLikeService {

    @Autowired
    UserLikeRepository reviewLikeRepository;
    @Autowired
    MovieLikeRepository movieLikeRepository;
    @Autowired
    UserLikeMapper reviewLikeMapper;
    @Autowired
    ReviewMapping reviewMapping;
    @Autowired
    MovieLikeMapper movieLikeMapper;
    @Autowired
    MovieMapping movieMapping;
    @Autowired
    UserMapping userMapping;

    @Override
    public List<MovieLikeDTO> getAllLikesForMovie(MovieDTO movie) {
        List<MovieLike> movieLikeList = movieLikeRepository
                .findMovieLikeByMovieId(movieMapping.mapMovieDtoToMovie(movie));
        if (movieLikeList.isEmpty())
            return Collections.emptyList();
        return movieLikeMapper.mapMovieLikeListToMovieLikeDtoList(movieLikeList);
    }

    public List<ReviewLikeDTO> getAllLikesForAReview(ReviewDTO review) {
        List<ReviewLike> reviewLikeList = reviewLikeRepository
                .findAllByReviewId(reviewMapping.mapReviewDtoToReview(review));
        if (reviewLikeList.isEmpty())
            return Collections.emptyList();
        return reviewLikeMapper.mapUserLikeListToUserLikeDtoList(reviewLikeList);
    }

    @Override
    public Optional<MovieLikeDTO> toggleMovieLike(MovieDTO movieDTO, UserDTO userDTO) {
        Movie movie = movieMapping.mapMovieDtoToMovie(movieDTO);
        User user = userMapping.mapUserDtoToUser(userDTO);
        Optional<MovieLike> movieLike = movieLikeRepository.findByMovieIdAndUserId(movie, user);
        if (movieLike.isPresent()) {
            movieLikeRepository.delete(movieLike.get());
            log.warn("user:{} disliked movie:{}", user.getId(), movie.getId());
            return Optional.empty();
        } else {
            return Optional.of(movieLikeMapper.mapMovieLikeToMovieLikeDto(movieLikeRepository.save(new MovieLike(user, movie))));
        }
    }


    public Optional<ReviewLikeDTO> toggleReviewLike(ReviewDTO reviewDTO, UserDTO userDTO) {
        Review review = reviewMapping.mapReviewDtoToReview(reviewDTO);
        User user = userMapping.mapUserDtoToUser(userDTO);
        Optional<ReviewLike> reviewLike = reviewLikeRepository.findByUserIdAndReviewId(user, review);
        if (reviewLike.isPresent()) {
            reviewLikeRepository.delete(reviewLike.get());
            log.warn("user:{} disliked review:{}", user.getId(), review.getId());
            return Optional.empty();
        } else {
            return Optional.of(reviewLikeMapper.mapUserLikeToUserLikeDto(reviewLikeRepository.save(new ReviewLike(user, review))));
        }
    }


}
