package com.avas.movieratingsystem.web.controller.feign;

import com.avas.movieratingsystem.model.ReviewDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

//TODO: Add environmental variables like url = "${CURRENCY_EXCHANGE_SERVICE_HOST:http://localhost}:8000")

@FeignClient(name= "review-microservice", url = "http://localhost:8100")
public interface ReviewMicroserviceProxy {

    @GetMapping("/api/v1/review/user/{userId}")
    public List<ReviewDTO> getReviewsForUser(@RequestParam Long userId);
}
