package com.mgs.movie.microservice.business.service.impl;

import com.mgs.library.business.exceptions.ResourceAlreadyExists;
import com.mgs.library.business.exceptions.ResourceNotFoundException;
import com.mgs.library.business.mappers.MovieMapping;
import com.mgs.library.business.repository.model.Movie;
import com.mgs.library.model.MovieDTO;
import com.mgs.movie.microservice.business.repository.MovieRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.mgs.movie.microservice.test.data.MovieTestData.createMovieDTO;
import static com.mgs.movie.microservice.test.data.MovieTestData.createMovieDtoList;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;

    @Autowired
    private MovieMapping movieMapping;
    @Mock
    private MovieMapping mockMovieMapping;

    @Spy
    @InjectMocks
    private MovieServiceImpl movieService;

    private MovieDTO movieDTO;
    private Movie movie;

    @BeforeEach
    void createTestData() {
        this.movieDTO = createMovieDTO();
        this.movie = movieMapping.mapMovieDtoToMovie(movieDTO);
    }

    @Test
    @DisplayName("Testing retrieval of all Movies")
    void testMoviesSuccessfully() {
        List<MovieDTO> movieDTOList = createMovieDtoList();
        List<Movie> movieList = movieMapping.mapMovieDtoListToMovieList(movieDTOList);
        when(movieRepository.findAll()).thenReturn(movieList);
        when(mockMovieMapping.mapMovieListToMovieListDto(movieList)).thenReturn(movieDTOList);
        List<MovieDTO> movieDTOListReturned = movieService.getAllMovies();
        Assertions.assertEquals(movieDTOList, movieDTOListReturned);
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Testing retrieval of empty Movie list")
    void testGetAllMoviesEmpty() {
        List<MovieDTO> emptyDtoList = new ArrayList<MovieDTO>();
        List<Movie> emptyMovieList = movieMapping.mapMovieDtoListToMovieList(emptyDtoList);
        when(movieRepository.findAll()).thenReturn(emptyMovieList);
        when(mockMovieMapping.mapMovieListToMovieListDto(emptyMovieList)).thenReturn(emptyDtoList);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> movieService.getAllMovies());
        verify(movieRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Testing deleting movie by id")
    void testDeletingMovieById() {
        MovieDTO movieDTO = createMovieDTO();
        doReturn(Optional.of(movieDTO)).when(movieService).findMovieById(1L);
        movieService.deleteMovieById(1L);
        verify(movieRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Testing when id is not null")
    void testDeleteByIdNull() {
        MovieDTO movieDTO = createMovieDTO();
        doReturn(Optional.empty()).when(movieService).findMovieById(1L);
        Assertions.assertThrows(ResourceAlreadyExists.class, () -> movieService.deleteMovieById(1L));
    }

    @Test
    @DisplayName("Testing finding movie by id")
    void testSuccessfullyFindingMovieById() {

        when(mockMovieMapping.mapMovieToMovieDto(movie)).thenReturn(movieDTO);
        when(movieRepository.findById(anyLong())).thenReturn(Optional.of(movie));
        movieService.findMovieById(anyLong());
        verify(movieRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Find non existing movie by id")
    @SuppressWarnings("squid:S5778")
    void testFailingFindMovieById() {
        when(movieRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> movieService.findMovieById(anyLong()));
    }

    @Test
    @DisplayName("Create a movie")
    void testSuccessfullyCreatingAMovie() {
        when(movieRepository.existsByTitle(movieDTO.getTitle())).thenReturn(false);
        when(mockMovieMapping.mapMovieDtoToMovie(movieDTO)).thenReturn(movie);
        when(movieRepository.save(movie)).thenReturn(movie);
        movieService.createMovie(movieDTO);
        verify(movieRepository, times(1)).save(movie);
    }

    @Test
    @DisplayName("Create a duplicate movie")
    void testFailingCreatingAMovie() {
        when(movieRepository.existsByTitle(anyString())).thenReturn(true);
        Assertions.assertThrows(ResourceAlreadyExists.class, () -> movieService.createMovie(movieDTO));
    }

    @Test
    @DisplayName("Update a movie by Id")
    void testSuccessfullyUpdatingAMovieById() {

        when(movieRepository.existsByTitle(anyString())).thenReturn(false);
        when(movieRepository.existsById(anyLong())).thenReturn(true);
        when(mockMovieMapping.mapMovieDtoToMovie(any(MovieDTO.class))).thenReturn(movie);
        when(mockMovieMapping.mapMovieToMovieDto(any(Movie.class))).thenReturn(movieDTO);
        when(movieRepository.save(movie)).thenReturn(movie);
        MovieDTO returnedMovieDto = movieService.updateMovieById(movieDTO, 1L);
        Assertions.assertEquals(movieDTO, returnedMovieDto);
        verify(movieRepository, times(1)).save(movie);

    }

    @Test
    @DisplayName("Update movie with taken title")
    void testFailUpdatingAmovieByIdTitleTaken() {
        when(movieRepository.existsByTitle(anyString())).thenReturn(true);
        Assertions.assertThrows(ResourceAlreadyExists.class, () -> movieService.updateMovieById(movieDTO, 1L));
    }

    @Test
    @DisplayName(("Update a movie which does not exist"))
    void testFailUpdatingAMovieWhichDoesNotExist() {
        when(movieRepository.existsByTitle(anyString())).thenReturn(false);
        when(movieRepository.existsById(anyLong())).thenReturn(false);
        Assertions.assertThrows(ResourceAlreadyExists.class, () -> movieService.updateMovieById(movieDTO, 1L));
    }

    @Test
    @DisplayName("Delete a movie")
    void testSuccessfullyDeleteAMovie() {
        doReturn(Optional.of(movieDTO)).when(movieService).findMovieById(anyLong());
        movieService.deleteMovieById(1L);
        verify(movieRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete a non existing movie")
    void testFailingDeleteAMovie() {
        doReturn(Optional.empty()).when(movieService).findMovieById(anyLong());
        Assertions.assertThrows(ResourceAlreadyExists.class, () -> movieService.deleteMovieById(1L));
    }
}
