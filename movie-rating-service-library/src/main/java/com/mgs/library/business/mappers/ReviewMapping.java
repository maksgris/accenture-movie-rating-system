package com.mgs.library.business.mappers;

import com.mgs.library.business.repository.model.Movie;
import com.mgs.library.business.repository.model.Review;
import com.mgs.library.business.repository.model.User;
import com.mgs.library.model.ReviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapping {
    @Mapping(source = "userId", target = "userId", qualifiedByName = "userIdToUserIdLong")
    @Mapping(source = "movieId", target = "movieId", qualifiedByName = "movieIdToMovieIdLong")
    ReviewDTO mapReviewToReviewDto(Review reviewEntity);

    @Mapping(source = "userId", target = "userId", qualifiedByName = "userIdLongToUserId")
    @Mapping(source = "movieId", target = "movieId", qualifiedByName = "movieIdLongToMovieId")
    Review mapReviewDtoToReview(ReviewDTO reviewDto);

    List<ReviewDTO> mapReviewListToReviewListDto(List<Review> reviewEntities);

    List<Review> mapReviewListDtoToReviewList(List<ReviewDTO> reviewEntities);

    @Named("userIdToUserIdLong")
    default Long userIdToUserIdLong(User userId) {
        return userId.getId();
    }

    @Named("userIdLongToUserId")
    default User userIdLongToUserId(Long userId) {
        return new User(userId);
    }

    @Named("movieIdToMovieIdLong")
    default Long movieIdToMovieIdLong(Movie movieId) {
        return movieId.getId();
    }

    @Named("movieIdLongToMovieId")
    default Movie movieIdLongToMovieId(Long movieId) {
        return new Movie(movieId);
    }
}
