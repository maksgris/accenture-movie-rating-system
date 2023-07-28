package com.mgs.user.like.microservice.controller.feign;

import com.mgs.library.model.ReviewDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;


@FeignClient(name = "review-microservice", url = "http://localhost:8100")
public interface ReviewMicroserviceProxy {

    @GetMapping("/api/v1/review/{reviewId}")
    public Optional<ReviewDTO> getReview(@PathVariable Long reviewId);
}
