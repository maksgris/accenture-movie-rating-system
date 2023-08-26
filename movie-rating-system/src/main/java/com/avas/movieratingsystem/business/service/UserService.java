package com.avas.movieratingsystem.business.service;

import com.avas.movieratingsystem.model.MovieDTO;
import com.avas.movieratingsystem.model.ReviewDTO;
import com.avas.movieratingsystem.model.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<UserDTO> getAllUsers();
    Optional<UserDTO> findUserById(Long id);
    void deleteUserById(Long id);
    UserDTO createUser(UserDTO newUser);
    UserDTO updateUser(UserDTO modifyExistingUser, Long id);
    List<MovieDTO> getAllMoviesReviewedByUserById(Long id);

}
