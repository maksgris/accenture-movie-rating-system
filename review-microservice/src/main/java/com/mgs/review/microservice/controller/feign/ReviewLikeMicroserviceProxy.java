package com.mgs.review.microservice.controller.feign;

import com.mgs.library.model.ReviewLikeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;


@FeignClient(name = "review-like-microservice", url = "http://localhost:8600")
public interface ReviewLikeMicroserviceProxy {

    @GetMapping("/api/v1/like/review/{reviewId}/")
    List<ReviewLikeDTO> getAllLikesForAReview(@PathVariable Long reviewId);
}
