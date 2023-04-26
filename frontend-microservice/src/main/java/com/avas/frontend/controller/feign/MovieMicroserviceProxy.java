package com.avas.frontend.controller.feign;

import com.avas.library.model.MovieDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "movie-microservice", url = "http://localhost:8200")
public interface MovieMicroserviceProxy {

    @GetMapping("/api/v1/movie/")
    public List<MovieDTO> getMovies();

    @GetMapping("/api/v1/movie/top10")
    public List<MovieDTO> getTopTenMovies();

    @GetMapping("/api/v1/movie/{movieId}")
    public List<MovieDTO> getMovies(@RequestParam Long movieId);

    @GetMapping("/api/v1/movie/random")
    public MovieDTO getRandomMovie();
}
