package com.mgs.user.like.microservice.controller;

import com.mgs.library.model.ReviewLikeDTO;
import com.mgs.user.like.microservice.business.service.UserLikeService;
import com.mgs.user.like.microservice.controller.feign.MovieMicroserviceProxy;
import com.mgs.user.like.microservice.controller.feign.ReviewMicroserviceProxy;
import com.mgs.user.like.microservice.controller.feign.UserMicroserviceProxy;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Log4j2
@Controller
@RequestMapping("api/v1/like")
@SuppressWarnings("all")
public class UserLikeController {

    @Autowired
    UserLikeService userLikeService;

    @Autowired
    UserMicroserviceProxy userMicroserviceProxy;
    @Autowired
    private MovieMicroserviceProxy movieMicroserviceProxy;
    @Autowired
    private ReviewMicroserviceProxy reviewMicroserviceProxy;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewLikeDTO>> getAllUserLikes(@PathVariable Long userId) {
        List<ReviewLikeDTO> userLikes = userLikeService.getAllUserLikes(userId);
        log.info("User with id:{} has {} likes", userId, userLikes.size());
        return new ResponseEntity<>(userLikes, HttpStatus.OK);
    }

    @GetMapping("/review/{reviewId}")
    public ResponseEntity<List<ReviewLikeDTO>> getAllLikesForReview(@PathVariable Long reviewId) {
        List<ReviewLikeDTO> userLikes = userLikeService.getAllLikesForAReview(reviewId);
        log.info("Review with id:{} has {} likes", reviewId, userLikes.size());
        return new ResponseEntity<>(userLikes, HttpStatus.OK);
    }

    @PutMapping("/review/{reviewId}/reviewer/{userId}")
    public ResponseEntity<ReviewLikeDTO> toggleReviewLike(@PathVariable Long reviewId, @PathVariable Long userId) {
        return userLikeService.toggleReviewLike(reviewId, userId)
                .map(likeDTO -> new ResponseEntity<>(likeDTO, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.OK));
    }


}
