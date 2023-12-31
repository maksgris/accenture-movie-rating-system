package com.mgs.user.microservice.web.controller;

import com.mgs.library.business.exceptions.ResourceNotFoundException;
import com.mgs.library.model.ReviewDTO;
import com.mgs.library.model.UserDTO;
import com.mgs.user.microservice.business.service.UserService;
import com.mgs.user.microservice.web.controller.feign.ReviewMicroserviceProxy;
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
@RequestMapping("api/v1/user")
@SuppressWarnings("all")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    private ReviewMicroserviceProxy reviewMicroserviceProxy;

    @GetMapping
    @ResponseBody
    public ResponseEntity<List<UserDTO>> getAllUsers() {

        List<UserDTO> userReviews = userService.getAllUsers();
        return ResponseEntity.ok(userReviews);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        Optional<UserDTO> foundUserDto = userService.findUserById(id);
        log.info("User found : {}", foundUserDto.get());
        return new ResponseEntity<>(foundUserDto.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Binding result error");
            return ResponseEntity.badRequest().build();
        }
        UserDTO savedUser = userService.createUser(userDTO);
        log.debug("New user is created : {}", userDTO);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id,
                                              @RequestBody UserDTO modifiedUserDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Binding result error");
            return ResponseEntity.badRequest().build();
        }
        UserDTO returnedUserDto = userService.updateUser(modifiedUserDto, id);
        log.debug("User with id: {} is now :{}", id, returnedUserDto);
        return new ResponseEntity<>(returnedUserDto, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserDTO> deleteUserById(@PathVariable Long id) {
        log.info("Delete User  by passing ID, where ID is:{}", id);
        userService.deleteUserById(id);
        log.debug("User with id {} is deleted", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

    @GetMapping("/{id}/reviews")
    public ResponseEntity<List<ReviewDTO>> getAllReviewsMadeByUser(@PathVariable Long id) {
        Optional<UserDTO> userDTO = userService.findUserById(id);
        userDTO.orElseThrow(() -> new ResourceNotFoundException("User with id:{0} is not found", id));
        List<ReviewDTO> reviewDTOList = reviewMicroserviceProxy.getReviewsForUser(id);
        return new ResponseEntity<>(reviewDTOList, HttpStatus.OK);
    }

}
