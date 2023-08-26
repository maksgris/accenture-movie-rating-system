package com.avas.movieratingsystem.web.controller;

import com.avas.movieratingsystem.business.service.MovieTypeService;
import com.avas.movieratingsystem.model.MovieTypeDTO;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;

@Log4j2
@Controller
@RequestMapping("api/v1/movie_type")
public class MovieTypeController {

    @Autowired
    MovieTypeService movieTypeService;

    @GetMapping
    public ResponseEntity<List<MovieTypeDTO>> getAllMovieTypes() {
        List<MovieTypeDTO> movieTypeList = movieTypeService.getAllMovieTypes();
        if (movieTypeList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movieTypeList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MovieTypeDTO> getMovieType(@PathVariable Long id) {
        Optional<MovieTypeDTO> foundMovieType = movieTypeService.findMovieTypeById(id);
        if (!foundMovieType.isPresent()) {
            log.warn("Movie type not found");
            return ResponseEntity.notFound().build();
        }
        log.info("Movie type found : {}", foundMovieType.get());
        return new ResponseEntity<>(foundMovieType.get(), HttpStatus.ACCEPTED);
    }

    @PostMapping
    public ResponseEntity<MovieTypeDTO> createMovieType(@RequestBody MovieTypeDTO movieTypeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Binding result error");
            return ResponseEntity.badRequest().build();
        }
        MovieTypeDTO savedMovieType = movieTypeService.createMovieType(movieTypeDTO);
        log.debug("New movie type is created : {}", movieTypeDTO);
        return new ResponseEntity<>(savedMovieType, HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<MovieTypeDTO> updateMovieType(@PathVariable Long id,
                                                @RequestBody MovieTypeDTO modifiedMovieTypeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Binding result error");
            return ResponseEntity.badRequest().build();
        }
        if (!movieTypeService.checkIfMovieTypeExistsById(id)) {
            log.info("Movie type with this id does not exist");
            return ResponseEntity.notFound().build();
        }
        MovieTypeDTO returnedMovieTypeDTO = movieTypeService.updateMovieTypeById(modifiedMovieTypeDTO , id);
        log.debug("Movie type with id: {} is now :{}", id, returnedMovieTypeDTO);
        return new ResponseEntity<>(returnedMovieTypeDTO,HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MovieTypeDTO> deleteMovieTypeById(@PathVariable Long id) {
        log.info("Delete Movie Type by passing ID, where ID is:{}", id);
        Optional<MovieTypeDTO> movieTypeDtoFound = movieTypeService.findMovieTypeById(id);
        if (!(movieTypeDtoFound.isPresent())) {
            log.warn("Movie type for delete with id {} is not found.", id);
            return ResponseEntity.notFound().build();
        }
        movieTypeService.deleteMovieTypeById(id);
        log.debug("Movie type with id {} is deleted", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
