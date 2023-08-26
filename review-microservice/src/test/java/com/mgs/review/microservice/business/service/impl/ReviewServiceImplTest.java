package com.mgs.review.microservice.business.service.impl;

import com.mgs.library.business.exceptions.ResourceAlreadyExists;
import com.mgs.library.business.exceptions.ResourceConflict;
import com.mgs.library.business.exceptions.ResourceNotFoundException;
import com.mgs.library.business.mappers.ReviewMapping;
import com.mgs.library.business.repository.model.Review;
import com.mgs.library.model.ReviewDTO;
import com.mgs.review.microservice.business.repository.ReviewRepository;
import com.mgs.review.microservice.test.data.ReviewTestData;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("squid:S5778")
class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Autowired
    private ReviewMapping reviewMapping;
    @Mock
    private ReviewMapping mockReviewMapping;

    @Spy
    @InjectMocks
    private ReviewServiceImpl reviewService;

    private ReviewDTO reviewDTO;
    private Review review;

    @BeforeEach
    void createTestData() {
        this.reviewDTO = ReviewTestData.createReviewDto();
        this.review = reviewMapping.mapReviewDtoToReview(reviewDTO);
    }

    @Test
    @DisplayName("Retrieval of all Reviews")
    void testReviewsSuccessfully() {
        List<ReviewDTO> reviewDtoList = ReviewTestData.createReviewDtoList();
        List<Review> reviewList = reviewMapping.mapReviewListDtoToReviewList(reviewDtoList);
        when(reviewRepository.findAll()).thenReturn(reviewList);
        when(mockReviewMapping.mapReviewListToReviewListDto(reviewList)).thenReturn(reviewDtoList);
        List<ReviewDTO> reviewDTOListReturned = reviewService.getAllReviews();
        Assertions.assertEquals(reviewDtoList, reviewDTOListReturned);
        verify(reviewRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deleting review by id")
    void testDeletingReviewById() {
        doReturn(Optional.of(reviewDTO)).when(reviewService).findReviewById(1L);
        reviewService.deleteReviewById(1L);
        verify(reviewRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Delete non existing review")
    void testDeleteNotFound() {
        doReturn(Optional.empty()).when(reviewService).findReviewById(1L);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> reviewService.deleteReviewById(1L));
    }

    @Test
    @DisplayName("Testing finding Review by id")
    void testSuccessfullyFindingReviewById() {
        when(mockReviewMapping.mapReviewToReviewDto(review)).thenReturn(reviewDTO);
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        reviewService.findReviewById(anyLong());
        verify(reviewRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Find non existing review by id")
    void testFailingFindMovieById() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> reviewService.findReviewById(anyLong()));
    }

    @Test
    @DisplayName("Create a review")
    void testSuccessfullyCreatingAReview() {
        when(reviewRepository.existsByMovieIdAndUserId(review.getMovieId(), review.getUserId())).thenReturn(false);
        when(mockReviewMapping.mapReviewDtoToReview(reviewDTO)).thenReturn(review);
        when(reviewRepository.save(review)).thenReturn(review);
        reviewService.createReview(reviewDTO);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    @DisplayName("Create a duplicate review")
    void testFailingCreatingAReview() {
        when(mockReviewMapping.mapReviewDtoToReview(reviewDTO)).thenReturn(review);
        when(reviewRepository.existsByMovieIdAndUserId(review.getMovieId(), review.getUserId())).thenReturn(true);
        Assertions.assertThrows(ResourceAlreadyExists.class, () -> reviewService.createReview(reviewDTO));
    }

    @Test
    @DisplayName("Update a review by Id")
    void testSuccessfullyUpdatingAReviewById() {
        when(reviewRepository.existsById(anyLong())).thenReturn(true);
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        when(mockReviewMapping.mapReviewToReviewDto(review)).thenReturn(reviewDTO);
        when(mockReviewMapping.mapReviewDtoToReview(reviewDTO)).thenReturn(review);
        when(reviewRepository.save(review)).thenReturn(review);
        ReviewDTO returnedReviewDto = reviewService.updateReviewById(reviewDTO, 1L);
        Assertions.assertEquals(reviewDTO, returnedReviewDto);
        verify(reviewRepository, times(1)).save(review);
    }

    @Test
    @DisplayName("Update non existing review")
    void testFailingToUpdateNonExistingReview() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> reviewService.updateReviewById(reviewDTO, 1L));
    }

    @Test
    @DisplayName(("Update a review with invalid movie id or user id"))
    void testFailUpdatingAMovieWhichDoesNotExist() {
        when(reviewRepository.findById(anyLong())).thenReturn(Optional.of(review));
        when(mockReviewMapping.mapReviewToReviewDto(review)).thenReturn(reviewDTO);
        when(reviewRepository.save(review)).thenReturn(review);
        Assertions.assertThrows(ResourceConflict.class, () -> reviewService.updateReviewById(ReviewTestData.createReviewDto(), 1L));
    }

    @Test
    @DisplayName("Delete a review")
    void testSuccessfullyDeleteAMovie() {
        doReturn(Optional.of(reviewDTO)).when(reviewService).findReviewById(anyLong());
        reviewService.deleteReviewById(1L);
        verify(reviewRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete a non existing review")
    void testFailingDeleteAMovie() {
        doReturn(Optional.empty()).when(reviewService).findReviewById(anyLong());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> reviewService.deleteReviewById(1L));
    }
}
