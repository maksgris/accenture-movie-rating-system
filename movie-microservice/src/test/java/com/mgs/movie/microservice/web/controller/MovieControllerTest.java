package com.mgs.movie.microservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.library.business.exceptions.ResourceAlreadyExists;
import com.mgs.library.business.exceptions.ResourceNotFoundException;
import com.mgs.library.model.MovieDTO;
import com.mgs.movie.microservice.business.service.MovieService;
import com.mgs.movie.microservice.controller.MovieController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Optional;

import static com.mgs.movie.microservice.test.data.MovieTestData.createMovieDtoListPredefined;
import static com.mgs.movie.microservice.test.data.MovieTestData.createMovieDtoPredefined;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MovieController.class)
class MovieControllerTest {
    static String URL = "/api/v1/movie";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieController movieController;

    @MockBean
    private MovieService movieService;

    private List<MovieDTO> movieDtoListPredefined;
    private MovieDTO movieDTO;

    @BeforeEach
    void beforeEach() {
        this.movieDtoListPredefined = createMovieDtoListPredefined();
        this.movieDTO = createMovieDtoPredefined();
    }

    @Test
    @DisplayName("Test endpoint to find all Movies succesfully")
    void findAllMovies() throws Exception {
        when(movieService.getAllMovies()).thenReturn(movieDtoListPredefined);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Testing Returns 3d"))
                .andExpect(status().isOk());
        verify(movieService, times(1)).getAllMovies();
    }

    @Test
    @DisplayName("Test endpoint to successfully finding a Movie by id")
    void findMovieById() throws Exception {
        when(movieService.findMovieById(anyLong())).thenReturn(Optional.of(movieDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Testing Returns 3d"))
                .andExpect(status().isOk());
        verify(movieService, times(1)).findMovieById(1L);
    }

    @Test
    @DisplayName("Test endpoint to find a non existing user by id")
    void findNonExistingMovieById() throws Exception {
        when(movieService.findMovieById(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/1"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(status().isNotFound());
        verify(movieService, times(1)).findMovieById(1L);
    }

    @Test
    @DisplayName("Test endpoint to find empty Movies list")
    void findAllMoviesEmpty() throws Exception {
        when(movieService.getAllMovies()).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(status().isNotFound());
        verify(movieService, times(1)).getAllMovies();

    }

    @Test
    @DisplayName("Test endpoint to create Movie")
    void createAMovie() throws Exception {
        movieDTO.setId(null);

        when(movieService.createMovie(movieDTO)).thenReturn(movieDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(new ObjectMapper().writeValueAsString(movieDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Testing Returns 3d"));

        verify(movieService, times(1)).createMovie(movieDTO);


    }

    @Test
    @DisplayName("Test endpoint to create a duplicate  Movie")
    void createAMovieDuplicate() throws Exception {
        movieDTO.setId(null);

        when(movieService.createMovie(movieDTO)).thenThrow(ResourceAlreadyExists.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(new ObjectMapper().writeValueAsString(movieDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        verify(movieService, times(1)).createMovie(movieDTO);
    }

    @Test
    @DisplayName("Test endpoint to update a Movie")
    void updateAMovie() throws Exception {
        when(movieService.updateMovieById(movieDTO, 1L)).thenReturn(movieDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(new ObjectMapper().writeValueAsString(movieDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Testing Returns 3d"));
        verify(movieService, times(1)).updateMovieById(movieDTO, 1L);
    }

    @Test
    @DisplayName("Test endpoint to update a Movie which does not exist")
    void updateANonExistingMovie() throws Exception {
        when(movieService.updateMovieById(movieDTO, 1L)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(new ObjectMapper().writeValueAsString(movieDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(status().isNotFound());
        verify(movieService, times(1)).updateMovieById(movieDTO, 1L);
    }

    @Test
    @DisplayName("Test endpoint to successfully deleting a Movie by id")
    void deleteMovieById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(movieService, times(1)).deleteMovieById(1L);
    }


}
