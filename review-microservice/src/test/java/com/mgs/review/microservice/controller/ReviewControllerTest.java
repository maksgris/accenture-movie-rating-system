package com.mgs.review.microservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.library.business.exceptions.ResourceAlreadyExists;
import com.mgs.library.business.exceptions.ResourceNotFoundException;
import com.mgs.library.model.ReviewDTO;
import com.mgs.review.microservice.business.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static com.mgs.review.microservice.test.data.ReviewTestData.createReviewDtoListPredefined;
import static com.mgs.review.microservice.test.data.ReviewTestData.createReviewDtoPredefined;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
class ReviewControllerTest {
    static String URL = "/api/v1/review";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewController movieController;

    @MockBean
    private ReviewService reviewService;

    private List<ReviewDTO> reviewDTOListPredefined;
    private ReviewDTO reviewDTO;

    @BeforeEach
    void beforeEach() {
        this.reviewDTOListPredefined = createReviewDtoListPredefined();
        this.reviewDTO = createReviewDtoPredefined();
    }

    @Test
    @DisplayName("Test endpoint to find all Reviews succesfully")
    void findAllReviews() throws Exception {
        when(reviewService.getAllReviews()).thenReturn(reviewDTOListPredefined);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].score").value(7))
                .andExpect(status().isOk());
        verify(reviewService, times(1)).getAllReviews();
    }


    @Test
    @DisplayName("Test endpoint to find empty Reviews list")
    void findAllReviewsEmpty() throws Exception {
        when(reviewService.getAllReviews()).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(status().isNotFound());
        verify(reviewService, times(1)).getAllReviews();

    }

    @Test
    @DisplayName("Test endpoint to find a non existing Review by id")
    void findNonExistingReviewById() throws Exception {
        when(reviewService.findReviewById(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/1"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(status().isNotFound());
        verify(reviewService, times(1)).findReviewById(1L);
    }

    @Test
    @DisplayName("Test endpoint to successfully finding a Review by id")
    void findReviewById() throws Exception {
        when(reviewService.findReviewById(anyLong())).thenReturn(Optional.of(reviewDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.score").value(7))
                .andExpect(status().isOk());
        verify(reviewService, times(1)).findReviewById(1L);
    }


    @Test
    @DisplayName("Test endpoint to create Review")
    void createAReview() throws Exception {
        reviewDTO.setId(null);

        when(reviewService.createReview(reviewDTO)).thenReturn(reviewDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(new ObjectMapper().writeValueAsString(reviewDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.score").value(7))
                .andExpect(status().isCreated());

        verify(reviewService, times(1)).createReview(reviewDTO);


    }

    @Test
    @DisplayName("Test endpoint to create a duplicate  Movie")
    void createAReviewDuplicate() throws Exception {
        reviewDTO.setId(null);

        when(reviewService.createReview(reviewDTO)).thenThrow(ResourceAlreadyExists.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(new ObjectMapper().writeValueAsString(reviewDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceAlreadyExists))
                .andExpect(status().isConflict());

        verify(reviewService, times(1)).createReview(reviewDTO);
    }

    @Test
    @DisplayName("Test endpoint to update a Movie")
    void updateAReview() throws Exception {
        when(reviewService.updateReviewById(reviewDTO, 1L)).thenReturn(reviewDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(new ObjectMapper().writeValueAsString(reviewDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.score").value(7))
                .andExpect(status().isAccepted());
        verify(reviewService, times(1)).updateReviewById(reviewDTO, 1L);
    }

    @Test
    @DisplayName("Test endpoint to update a Review which does not exist")
    void updateANonExistingReview() throws Exception {
        when(reviewService.updateReviewById(reviewDTO, 1L)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(new ObjectMapper().writeValueAsString(reviewDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(status().isNotFound());
        verify(reviewService, times(1)).updateReviewById(reviewDTO, 1L);
    }

    @Test
    @DisplayName("Test endpoint to successfully deleting a Review by id")
    void deleteReviewById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(reviewService, times(1)).deleteReviewById(1L);
    }

}
