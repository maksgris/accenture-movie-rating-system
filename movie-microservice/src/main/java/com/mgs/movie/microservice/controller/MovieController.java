package com.mgs.movie.microservice.controller;

import com.mgs.library.business.exceptions.ResourceNotFoundException;
import com.mgs.library.model.MovieDTO;
import com.mgs.library.model.MovieTypeDTO;
import com.mgs.movie.microservice.business.service.MovieService;
import com.mgs.movie.microservice.controller.feign.MovieTypeMicroserviceProxy;
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
@RequestMapping("api/v1/movie")
@SuppressWarnings("all")
public class MovieController {

    @Autowired
    MovieService movieService;
    @Autowired
    MovieTypeMicroserviceProxy movieTypeMicroserviceProxy;

    @GetMapping("/top10")
    public ResponseEntity<List<MovieDTO>> getTopTenMovies() {
        return new ResponseEntity<>(movieService.getTopTenMovies(), HttpStatus.OK);
    }

    @GetMapping("/random")
    public ResponseEntity<MovieDTO> getRandomMovie() {
        return new ResponseEntity<>(movieService.getRandomMovie(), HttpStatus.OK);
    }

    @GetMapping("/movie_type/{genre}")
    public ResponseEntity<List<MovieDTO>> getMovieOfAGenre(@PathVariable String genre) {
        Optional<MovieTypeDTO> optionalMovieTypeDTO = movieTypeMicroserviceProxy.getMovieType(genre);
        log.info("Genre found :{}", optionalMovieTypeDTO.get().getType());
        optionalMovieTypeDTO
                .orElseThrow(() -> new ResourceNotFoundException("Genre not found"));
        return new ResponseEntity<>(movieService.getMovieOfAGenre(optionalMovieTypeDTO.get()), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        List<MovieDTO> movieList = movieService.getAllMovies();
        return ResponseEntity.ok(movieList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieDTO> getMovieById(@PathVariable Long id) {
        Optional<MovieDTO> foundMovie = movieService.findMovieById(id);
        log.info("Movie found : {}", foundMovie.get());
        return new ResponseEntity<>(foundMovie.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MovieDTO> createMovie(@RequestBody MovieDTO movieDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Binding result error");
            return ResponseEntity.badRequest().build();
        }
        MovieDTO savedMovie = movieService.createMovie(movieDto);
        log.debug("New movie is created : {}", movieDto);
        return new ResponseEntity<>(savedMovie, HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieDTO> updateMovie(@PathVariable Long id,
                                                @RequestBody MovieDTO modifiedMovieDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Binding result error");
            return ResponseEntity.badRequest().build();
        }
        MovieDTO returnedMovieDTO = movieService.updateMovieById(modifiedMovieDTO, id);
        log.debug("Movie with id: {} is now :{}", id, returnedMovieDTO);
        return new ResponseEntity<>(returnedMovieDTO, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MovieDTO> deleteMovieById(@PathVariable Long id) {
        log.info("Delete Movie by passing ID, where ID is:{}", id);
        movieService.deleteMovieById(id);
        log.debug("Movie with id {} is deleted", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
