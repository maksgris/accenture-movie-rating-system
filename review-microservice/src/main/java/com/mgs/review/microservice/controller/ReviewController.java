package com.mgs.review.microservice.controller;


import com.mgs.library.model.ReviewDTO;
import com.mgs.review.microservice.business.service.ReviewService;
import com.mgs.review.microservice.controller.feign.LikeMicroserviceProxy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
@RequestMapping("api/v1/review")
public class ReviewController {


    @Autowired
    ReviewService reviewService;
    @Autowired
    private LikeMicroserviceProxy reviewLikeMicroserviceProxy;

    @GetMapping("/movie/{movieId}")
    public ResponseEntity<List<ReviewDTO>> getAllReviewsForAMovie(@PathVariable Long movieId) {
        List<ReviewDTO> reviewList = reviewService.getAllReviewsForAMovie(movieId);
        return ResponseEntity.ok(reviewList);
    }

    @GetMapping("/movie/top/{movieId}")
    public ResponseEntity<ReviewDTO> getTopReviewsForAMovie(@PathVariable Long movieId) {
        ReviewDTO topReview = reviewService.getTopReviewForAMovie(movieId);
        return ResponseEntity.ok(topReview);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDTO>> getAllReviewsForUser(@PathVariable Long userId) {
        List<ReviewDTO> reviewList = reviewService.getAllReviewsMadeByUserById(userId);
        return ResponseEntity.ok(reviewList);
    }

    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<ReviewDTO> reviewList = reviewService.getAllReviews();
        return ResponseEntity.ok(reviewList);
    }

    @GetMapping("/{id}")
    @SuppressWarnings("squid:S3655")
    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable Long id) {
        Optional<ReviewDTO> foundReview = reviewService.findReviewById(id);
        log.info("Review found : {}", foundReview.get());
        return new ResponseEntity<>(foundReview.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(@RequestBody ReviewDTO reviewDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Binding result error");
            return ResponseEntity.badRequest().build();
        }
        ReviewDTO savedReview = reviewService.createReview(reviewDTO);
        log.debug("New review is created : {}", reviewDTO);
        return new ResponseEntity<>(savedReview, HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewDTO> updateReview(@PathVariable Long id,
                                                  @RequestBody ReviewDTO modifiedReviewDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Binding result error");
            return ResponseEntity.badRequest().build();
        }
        ReviewDTO returnedReviewDto = reviewService.updateReviewById(modifiedReviewDto, id);
        log.debug("Review with id: {} is now :{}", id, returnedReviewDto);
        return new ResponseEntity<>(returnedReviewDto, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReviewDTO> deleteReviewById(@PathVariable Long id) {
        log.info("Delete Review by passing ID, where ID is:{}", id);
        reviewService.deleteReviewById(id);
        log.debug("Review with id {} is deleted", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
