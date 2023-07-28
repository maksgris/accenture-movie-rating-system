package com.mgs.frontend.controller.feign;

import com.mgs.library.model.ReviewDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "review-microservice", url = "http://localhost:8100")
public interface ReviewMicroserviceProxy {

    @GetMapping("/api/v1/review/movie/top/{movieId}")
    public ReviewDTO getTopReview(@RequestParam Long movieId);

    @GetMapping("api/v1/review/movie/{movieId}")
    public List<ReviewDTO> getAllReviewsForMovie(@RequestParam Long movieId);
}
