package com.mgs.movie.microservice.business.service.impl;

import com.mgs.library.business.exceptions.ResourceAlreadyExists;
import com.mgs.library.business.exceptions.ResourceNotFoundException;
import com.mgs.library.business.mappers.MovieMapping;
import com.mgs.library.business.mappers.MovieTypeMapping;
import com.mgs.library.business.repository.model.Movie;
import com.mgs.library.model.MovieDTO;
import com.mgs.library.model.MovieTypeDTO;
import com.mgs.movie.microservice.business.repository.MovieRepository;
import com.mgs.movie.microservice.business.service.MovieService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Service
public class MovieServiceImpl implements MovieService {
    @Autowired
    MovieRepository movieRepository;
    @Autowired
    MovieMapping movieMapper;
    @Autowired
    MovieTypeMapping movieTypeMapping;

    @Override
    public List<MovieDTO> getTopTenMovies() {
        Map<MovieDTO, Long> top10 = new HashMap<>();
        List<MovieDTO> movieDTOList = getAllMovies();
        for (MovieDTO movieDTO : movieDTOList) {
            top10.put(movieDTO, movieRepository.countChildrenByParent(movieMapper.mapMovieDtoToMovie(movieDTO))
            );
        }
        Map<MovieDTO, Long> topTen = top10.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(10)
                .collect(Collectors.toMap(
                        Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
        return new ArrayList<>(topTen.keySet());
    }

    @Override
    public MovieDTO getRandomMovie() {
        return movieMapper.mapMovieToMovieDto(movieRepository.findRandomMovie());
    }

    public List<MovieDTO> getMovieOfAGenre(MovieTypeDTO movieGenre) {
        List<Movie> movieList = movieRepository.findMovieByMovieType(movieTypeMapping.mapMovieTypeDtoToMovieType(movieGenre));
        log.info("Movies list size is : {}", movieList.size());
        if (movieList.isEmpty())
            throw new ResourceNotFoundException("No movies found");
        log.info("movie list size is :{}", movieList.size());
        return movieMapper.mapMovieListToMovieListDto(movieList);
    }

    public List<MovieDTO> getAllMovies() {
        List<Movie> returnedMovieList = movieRepository.findAll();
        if (returnedMovieList.isEmpty())
            throw new ResourceNotFoundException("No movies found");
        log.info("movie list size is :{}", returnedMovieList.size());
        return movieMapper.mapMovieListToMovieListDto(returnedMovieList);

    }

    @SuppressWarnings("squid:S2201")
    public Optional<MovieDTO> findMovieById(Long id) {
        Optional<MovieDTO> foundMovieDto = movieRepository.findById(id)
                .map(foundUser -> movieMapper.mapMovieToMovieDto(foundUser));
        foundMovieDto.orElseThrow(() -> new ResourceNotFoundException("Movie with id:{0} Not found", id));
        log.info("Found movie :{}", foundMovieDto);
        return foundMovieDto;
    }

    @SuppressWarnings("squid:S2201")
    public void deleteMovieById(Long id) {
        findMovieById(id)
                .orElseThrow(() -> new ResourceAlreadyExists("Movie for delete with id {0} is not found.", id));
        movieRepository.deleteById(id);
        log.info("Movie with id: {} is deleted", id);

    }

    public MovieDTO createMovie(MovieDTO newMovie) {
        boolean movieAlreadyExists = movieRepository.existsByTitle(newMovie.getTitle());
        if (movieAlreadyExists) {
            log.warn("Can not create movie, movie with this title already exists");
            throw new ResourceAlreadyExists("Can not create movie, movie with this title already exists");
        }
        Movie savedMovie = movieRepository.save(movieMapper.mapMovieDtoToMovie(newMovie));
        log.info("Movie is created : {}", savedMovie);
        return movieMapper.mapMovieToMovieDto(savedMovie);
    }

    public MovieDTO updateMovieById(MovieDTO modifyExistingMovie, Long id) {
        boolean movieAlreadyExists = movieRepository.existsByTitle(modifyExistingMovie.getTitle());
        if (movieAlreadyExists) {
            log.warn("Can not update movie. This movie title is already taken :{}", modifyExistingMovie.getMovieType());
            throw new ResourceAlreadyExists("Can not update movie. This movie title is already taken");
        }
        if (!movieRepository.existsById(id)) {
            throw new ResourceAlreadyExists("Movie with this id:{0} does not exist", id);
        }
        modifyExistingMovie.setId(id);
        Movie modifiedMovie = movieRepository.save(movieMapper.mapMovieDtoToMovie(modifyExistingMovie));
        log.info("Movie is updated movie id :{}, movie is now :{}", modifiedMovie.getId(), modifiedMovie);
        return movieMapper.mapMovieToMovieDto(modifiedMovie);
    }

}
