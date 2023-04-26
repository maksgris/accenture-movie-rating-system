package com.avas.frontend.controller.feign;

import com.avas.library.model.MovieLikeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "user-like-microservice", url = "http://localhost:8600")
public interface UserLikeMicroserviceProxy {

    @GetMapping("/api/v1/like/movie/{movieId}")
    public List<MovieLikeDTO> getLikesForAMovie(@RequestParam Long movieId);


}
