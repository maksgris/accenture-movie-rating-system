package com.mgs.user.like.microservice.business.service.impl;

import com.mgs.library.business.exceptions.ResourceNotFoundException;
import com.mgs.library.business.mappers.ReviewMapping;
import com.mgs.library.business.mappers.UserLikeMapper;
import com.mgs.library.business.mappers.UserMapping;
import com.mgs.library.business.repository.model.ReviewLike;
import com.mgs.library.model.ReviewDTO;
import com.mgs.library.model.ReviewLikeDTO;
import com.mgs.library.model.UserDTO;
import com.mgs.user.like.microservice.business.repository.ReviewRepository;
import com.mgs.user.like.microservice.business.repository.UserLikeRepository;
import com.mgs.user.like.microservice.business.repository.UserRepository;
import com.mgs.user.like.microservice.business.service.UserLikeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@SuppressWarnings("all")
public class UserLikeServiceImpl implements UserLikeService {

    @Autowired
    UserLikeRepository userLikeRepository;
    @Autowired
    UserLikeMapper userLikeMapper;
    @Autowired
    ReviewRepository reviewRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    ReviewMapping reviewMapping;
    @Autowired
    UserMapping userMapping;

    public List<ReviewLikeDTO> getAllUserLikes(Long userId) {
        Optional<UserDTO> user = userRepository.findById(userId)
                .map(returnedUser -> userMapping.mapUserToUserDto(returnedUser));
        user.orElseThrow(() -> new ResourceNotFoundException("User with id:{0} does not exist", userId));
        List<ReviewLikeDTO> reviewLikeDTOList = userLikeRepository
                .findAllByUserId(userMapping.mapUserDtoToUser(user.get())).stream()
                .map(userLikeMapper::mapUserLikeToUserLikeDto).collect(Collectors.toList());
        if (reviewLikeDTOList.isEmpty())
            throw new ResourceNotFoundException("User with user id:{0} has no likes", userId);
        else
            return reviewLikeDTOList;
    }

    public Optional<ReviewLikeDTO> toggleReviewLike(Long reviewId, Long reviewerUserId) {
        Optional<ReviewDTO> reviewDTO = reviewRepository.findById(reviewId)
                .map(review -> reviewMapping.mapReviewToReviewDto(review));
        Optional<UserDTO> foundUserDTO = userRepository.findById(reviewerUserId)
                .map(foundUser -> userMapping.mapUserToUserDto(foundUser));
        if (!reviewDTO.isPresent() || !foundUserDTO.isPresent())
            throw new ResourceNotFoundException("invalid reviewer id or review id");
        if (userLikeRepository.existsByUserIdAndReviewId(userMapping.mapUserDtoToUser(foundUserDTO.get())
                , reviewMapping.mapReviewDtoToReview(reviewDTO.get()))) {
            log.warn("user:{} disliked review:{}", foundUserDTO.get(), reviewDTO.get());
            userLikeRepository.delete(userLikeRepository.findByUserIdAndReviewId(userMapping.mapUserDtoToUser(foundUserDTO.get())
                    , reviewMapping.mapReviewDtoToReview(reviewDTO.get())).get());
            return Optional.empty();
        }
        log.warn("user:{} liked review:{}", foundUserDTO.get(), reviewDTO.get());
        ReviewLike userLike = userLikeRepository.save(new ReviewLike(userMapping.mapUserDtoToUser(foundUserDTO.get())
                , reviewMapping.mapReviewDtoToReview(reviewDTO.get())));
        log.warn("user like:{}", userLike);
        return Optional.of(userLikeMapper.mapUserLikeToUserLikeDto(userLike));
    }

    public List<ReviewLikeDTO> getAllLikesForAReview(Long reviewId) {
        Optional<ReviewDTO> review = reviewRepository.findById(reviewId)
                .map(reviewMapping::mapReviewToReviewDto);
        review.orElseThrow(() -> new ResourceNotFoundException("Review with id:{0} does not exist", reviewId));
        List<ReviewLikeDTO> ReviewLikeDTOList = userLikeRepository.findAllByReviewId(reviewMapping
                        .mapReviewDtoToReview(review.get())).stream()
                .map(userLikeMapper::mapUserLikeToUserLikeDto).collect(Collectors.toList());
        if (ReviewLikeDTOList.isEmpty())
            throw new ResourceNotFoundException("Review with review id:{0} has no likes", reviewId);
        else
            return ReviewLikeDTOList;
    }

}
