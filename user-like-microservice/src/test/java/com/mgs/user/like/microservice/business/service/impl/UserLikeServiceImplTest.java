package com.mgs.user.like.microservice.business.service.impl;


import com.mgs.library.business.exceptions.ResourceNotFoundException;
import com.mgs.library.business.mappers.ReviewMapping;
import com.mgs.library.business.mappers.UserLikeMapper;
import com.mgs.library.business.mappers.UserMapping;
import com.mgs.library.business.repository.model.Review;
import com.mgs.library.business.repository.model.ReviewLike;
import com.mgs.library.business.repository.model.User;
import com.mgs.library.model.ReviewDTO;
import com.mgs.library.model.ReviewLikeDTO;
import com.mgs.library.model.UserDTO;
import com.mgs.user.like.microservice.business.repository.ReviewRepository;
import com.mgs.user.like.microservice.business.repository.UserLikeRepository;
import com.mgs.user.like.microservice.business.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mgs.user.like.microservice.test.data.ReviewTestData.createReviewDto;
import static com.mgs.user.like.microservice.test.data.UserLikeTestData.createUserLikeDTO;
import static com.mgs.user.like.microservice.test.data.UserLikeTestData.createUserLikeDtoList;
import static com.mgs.user.like.microservice.test.data.UserTestData.createUserDto;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserLikeServiceImplTest {

    @Mock
    private UserLikeRepository userLikeRepository;
    @Mock
    private UserRepository userRepository;

    @Autowired
    private UserLikeMapper userLikeMapper;
    @Autowired
    private UserMapping userMapping;
    @Mock
    private UserMapping mockUserMapping;
    @Mock
    private UserLikeMapper mockUserLikeMapper;

    @Mock
    private ReviewRepository reviewRepository;
    @Autowired
    private ReviewMapping reviewMapping;
    @Mock
    private ReviewMapping mockReviewMapping;
    @Spy
    @InjectMocks
    private UserLikeServiceImpl userLikeService;

    private ReviewLikeDTO userLikeDTO;
    private ReviewLike userLike;

    @BeforeEach
    public void createTestData() {
        this.userLikeDTO = createUserLikeDTO();
        this.userLike = userLikeMapper.mapUserLikeDtoToUserLike(userLikeDTO);
    }

    @Test
    @DisplayName("Retrieval of all UserLikes for a user")
    void testSuccessfullyGetAllUserLikesForUser() {
        UserDTO userDto = createUserDto();
        List<ReviewLikeDTO> userLikeDtoList = createUserLikeDtoList();
        User user = userMapping.mapUserDtoToUser(userDto);
        when(mockUserMapping.mapUserToUserDto(user)).thenReturn(userDto);
        when(mockUserMapping.mapUserDtoToUser(userDto)).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userLikeRepository.findAllByUserId(user)).thenReturn(userLikeMapper
                .mapUserLikeDtoListToUserLikeList(userLikeDtoList));
        when(mockUserLikeMapper.mapUserLikeToUserLikeDto(userLike)).thenReturn(userLikeDTO);
        List<ReviewLikeDTO> userLikeDTOList = userLikeService.getAllUserLikes(anyLong());
        Assertions.assertNotEquals(0, userLikeDTOList.size());
        verify(userLikeRepository, times(1)).findAllByUserId(user);
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Retrieval of all UserLikes for a non existing user")
    void testFailGetAllUserLikesForNonExistingUser() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userLikeService.getAllUserLikes(1L));
    }

    @Test
    @DisplayName("Retrieval of empty user likes list for a user")
    void testFailGetAllUserLikesEmpty() {
        UserDTO userDto = createUserDto();
        User user = userMapping.mapUserDtoToUser(userDto);
        List<ReviewLikeDTO> userLikeDtoList = new ArrayList<ReviewLikeDTO>();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userLikeRepository.findAllByUserId(user)).thenReturn(userLikeMapper
                .mapUserLikeDtoListToUserLikeList(userLikeDtoList));
        when(mockUserLikeMapper.mapUserLikeToUserLikeDto(userLike)).thenReturn(userLikeDTO);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userLikeService.getAllUserLikes(1L));
    }


    @Test
    @DisplayName("Retrieval of all UserLikes for a review")
    void testSuccessfullyGetAllUserLikesForReview() {
        ReviewDTO reviewDTO = createReviewDto();
        List<ReviewLikeDTO> userLikeDtoList = createUserLikeDtoList();
        Review review = reviewMapping.mapReviewDtoToReview(reviewDTO);
        when(mockReviewMapping.mapReviewToReviewDto(review)).thenReturn(reviewDTO);
        when(mockReviewMapping.mapReviewDtoToReview(reviewDTO)).thenReturn(review);
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        when(userLikeRepository.findAllByReviewId(review)).thenReturn(userLikeMapper
                .mapUserLikeDtoListToUserLikeList(userLikeDtoList));
        when(mockUserLikeMapper.mapUserLikeToUserLikeDto(userLike)).thenReturn(userLikeDTO);
        List<ReviewLikeDTO> userLikeDTOList = userLikeService.getAllLikesForAReview(anyLong());
        Assertions.assertNotEquals(0, userLikeDTOList.size());
        verify(userLikeRepository, times(1)).findAllByReviewId(review);
        verify(reviewRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Retrieval of all UserLikes for a non existing review")
    void testFailGetAllUserLikesForNonExistingReview() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userLikeService.getAllLikesForAReview(1L));
    }

    @Test
    @DisplayName("Retrieval of empty user likes list for a review")
    void testFailGetAllUserLikesEmptyForReview() {
        ReviewDTO reviewDTO = createReviewDto();
        Review review = reviewMapping.mapReviewDtoToReview(reviewDTO);
        List<ReviewLikeDTO> userLikeDtoList = new ArrayList<>();
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        when(userLikeRepository.findAllByReviewId(review)).thenReturn(userLikeMapper
                .mapUserLikeDtoListToUserLikeList(userLikeDtoList));
        when(mockUserLikeMapper.mapUserLikeToUserLikeDto(userLike)).thenReturn(userLikeDTO);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userLikeService.getAllLikesForAReview(1L));
    }

    @Test
    @DisplayName("Toggle Review Like from like to dislike")
    void testToggleReviewFromDislikeToLike() {
        ReviewDTO reviewDTO = createReviewDto();
        Review review = reviewMapping.mapReviewDtoToReview(reviewDTO);
        when(mockReviewMapping.mapReviewToReviewDto(review)).thenReturn(reviewDTO);
        when(mockReviewMapping.mapReviewDtoToReview(reviewDTO)).thenReturn(review);
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        UserDTO userDto = createUserDto();
        User user = userMapping.mapUserDtoToUser(userDto);
        when(mockUserMapping.mapUserToUserDto(user)).thenReturn(userDto);
        when(mockUserMapping.mapUserDtoToUser(userDto)).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));

        when(userLikeRepository.existsByUserIdAndReviewId(user, review)).thenReturn(true);
        when(userLikeRepository.findByUserIdAndReviewId(user, review)).thenReturn(Optional.of(userLike));
        userLikeService.toggleReviewLike(1L, 1L);
        verify(userLikeRepository, times(1)).delete(userLike);
    }

    @Test
    @DisplayName("Toggle Review Like from dislike to like")
    void testToggleReviewLikeFromLikeToDislike() {
        ReviewDTO reviewDTO = createReviewDto();
        Review review = reviewMapping.mapReviewDtoToReview(reviewDTO);
        when(mockReviewMapping.mapReviewToReviewDto(review)).thenReturn(reviewDTO);
        when(mockReviewMapping.mapReviewDtoToReview(reviewDTO)).thenReturn(review);
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        UserDTO userDto = createUserDto();
        User user = userMapping.mapUserDtoToUser(userDto);
        when(mockUserMapping.mapUserToUserDto(user)).thenReturn(userDto);
        when(mockUserMapping.mapUserDtoToUser(userDto)).thenReturn(user);
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userLikeRepository.existsByUserIdAndReviewId(user, review)).thenReturn(false);
        when(userLikeRepository.save(any(ReviewLike.class))).thenReturn(userLike);
        when(mockUserLikeMapper.mapUserLikeToUserLikeDto(any(ReviewLike.class))).thenReturn(userLikeDTO);
        userLikeService.toggleReviewLike(1L, 1L);
        verify(userLikeRepository, times(1)).save(any(ReviewLike.class));
    }

}
