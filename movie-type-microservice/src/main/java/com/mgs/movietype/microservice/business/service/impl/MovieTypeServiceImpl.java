package com.mgs.movietype.microservice.business.service.impl;

import com.mgs.library.business.exceptions.ResourceAlreadyExists;
import com.mgs.library.business.exceptions.ResourceNotFoundException;
import com.mgs.library.business.mappers.MovieTypeMapping;
import com.mgs.library.business.repository.model.MovieType;
import com.mgs.library.model.MovieTypeDTO;
import com.mgs.movietype.microservice.business.repository.MovieTypeRepository;
import com.mgs.movietype.microservice.business.service.MovieTypeService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Log4j2
@Service
@SuppressWarnings("squid:S2201")
public class MovieTypeServiceImpl implements MovieTypeService {
    @Autowired
    MovieTypeRepository movieTypeRepository;

    @Autowired
    MovieTypeMapping movieTypeMapping;

    @Override
    public Optional<MovieTypeDTO> getMovieTypeByName(String movieTypeName) {
        Optional<MovieTypeDTO> movieTypeDTO = movieTypeRepository.findMovieTypeByType(movieTypeName)
                .map(movieType -> movieTypeMapping.mapMovieTypeToMovieTypeDto(movieType));
        movieTypeDTO.orElseThrow(() -> new ResourceNotFoundException("movieType with name:" + movieTypeName + " does not exist"));
        log.info("Found movie type :{}", movieTypeDTO);
        return movieTypeDTO;
    }

    public List<MovieTypeDTO> getAllMovieTypes() {
        List<MovieType> returnedUserList = movieTypeRepository.findAll();
        if (returnedUserList.isEmpty())
            throw new ResourceNotFoundException("No users found");
        log.info("user list size is :{}", returnedUserList.size());
        return movieTypeMapping.mapMovieTypeListToMovieTypeListDto(returnedUserList);

    }

    public Optional<MovieTypeDTO> findMovieTypeById(Long id) {
        Optional<MovieTypeDTO> movieTypeDTO = movieTypeRepository.findById(id)
                .map(movieType -> movieTypeMapping.mapMovieTypeToMovieTypeDto(movieType));
        movieTypeDTO.orElseThrow(() -> new ResourceNotFoundException("movieType with id:{0} does not exist", id));
        log.info("Found movie type :{}", movieTypeDTO);
        return movieTypeDTO;
    }

    public void deleteMovieTypeById(Long id) {
        findMovieTypeById(id)
                .orElseThrow(() -> new ResourceNotFoundException("MovieType for delete with id {0} is not found.", id));
        movieTypeRepository.deleteById(id);
        log.info("movie type with id: {} is deleted", id);
    }

    public MovieTypeDTO createMovieType(MovieTypeDTO newMovieType) {
        boolean movieTypeAlreadyExists = movieTypeRepository.existsByType(newMovieType.getType());
        if (movieTypeAlreadyExists) {
            throw new ResourceAlreadyExists("Can not create movie type, movie type already exists");
        }
        MovieType savedMovieType = movieTypeRepository.save(movieTypeMapping.mapMovieTypeDtoToMovieType(newMovieType));
        log.info("movie type  is created : {}", savedMovieType);
        return movieTypeMapping.mapMovieTypeToMovieTypeDto(savedMovieType);
    }

    public MovieTypeDTO updateMovieTypeById(MovieTypeDTO modifyExistingMovieType, Long id) {
        if (!movieTypeRepository.existsById(id))
            throw new ResourceNotFoundException("Movie type with id:{0} is not found", id);
        if (movieTypeRepository.existsByType(modifyExistingMovieType.getType())) {
            throw new ResourceAlreadyExists("Can not update movie type. This movie type is already taken");
        }
        modifyExistingMovieType.setId(id);
        MovieType modifiedMovieType = movieTypeRepository.save(movieTypeMapping.mapMovieTypeDtoToMovieType(modifyExistingMovieType));
        log.info("movie type is updated id :{}, movie type is now :{}", modifiedMovieType.getId(), modifiedMovieType);
        return movieTypeMapping.mapMovieTypeToMovieTypeDto(modifiedMovieType);
    }

}
