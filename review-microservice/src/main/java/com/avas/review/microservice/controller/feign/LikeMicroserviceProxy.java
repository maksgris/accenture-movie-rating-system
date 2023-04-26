package com.avas.review.microservice.controller.feign;

import com.avas.library.model.MovieTypeDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;


@FeignClient(name = "movie-type-microservice", url = "http://localhost:8500")
public interface LikeMicroserviceProxy {

    @GetMapping("/api/v1/movie_type/name/{genre}")
    public Optional<MovieTypeDTO> getMovieType(@PathVariable String genre);
}
