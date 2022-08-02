package com.avas.movieratingsystem.business.mappers;


import com.avas.movieratingsystem.business.repository.model.Movie;
import com.avas.movieratingsystem.business.repository.model.Review;
import com.avas.movieratingsystem.business.repository.model.User;
import com.avas.movieratingsystem.business.repository.model.UserType;
import com.avas.movieratingsystem.model.MoviesByUserDTO;
import com.avas.movieratingsystem.model.UserDTO;
import com.avas.movieratingsystem.model.UserReviewDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapping {
    @Mappings({
            @Mapping(source = "userType", target = "userType", qualifiedByName = "userTypeToString")
    })
    UserDTO mapUserToUserDto(User UserEntity);
    @Mappings({
            @Mapping(source = "userType", target = "userType", qualifiedByName = "userTypeStringToUserTypeDto")
    })
    User mapUserDtoToUser(UserDTO userDto);
    List<UserReviewDTO> mapReviewListToUserReviewDto(List<Review> userEntities);
    List<MoviesByUserDTO> mapMovieListForUserToMovieListDto(List<Movie> movieList);

    @Named("userTypeToString")
    default String userUserTypeToUserTypeDto(UserType userType){
        return userType.getType();
    }
    @Named("userTypeStringToUserTypeDto")
    default UserType userUserTypeToUserTypeDto(String userType){
        return new UserType(userType);
    }

}
