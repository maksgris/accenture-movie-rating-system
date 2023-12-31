package com.mgs.user.like.microservice.controller.feign;

import com.mgs.library.model.MovieDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;


@FeignClient(name = "movie-microservice", url = "http://localhost:8200")
public interface MovieMicroserviceProxy {

    @GetMapping("/api/v1/movie/{movieId}")
    Optional<MovieDTO> getMovie(@PathVariable Long movieId);
}
