package com.mgs.usertype.microservice.controller;

import com.mgs.library.model.UserTypeDTO;
import com.mgs.usertype.microservice.business.service.UserTypeService;
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
@RequestMapping("api/v1/user_type")
public class UserTypeController {

    @Autowired
    UserTypeService userTypeService;

    @GetMapping
    public ResponseEntity<List<UserTypeDTO>> getAllUserTypes() {
        List<UserTypeDTO> userTypeDTOS = userTypeService.getAllUserTypes();
        return ResponseEntity.ok(userTypeDTOS);
    }

    @GetMapping("/{id}")
    @SuppressWarnings("squid:S3655")
    public ResponseEntity<UserTypeDTO> getUserTypeById(@PathVariable Long id) {
        Optional<UserTypeDTO> foundUserTypeDTO = userTypeService.findUserTypeById(id);
        log.info("User type found : {}", foundUserTypeDTO.get());
        return new ResponseEntity<>(foundUserTypeDTO.get(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<UserTypeDTO> createUserType(@RequestBody UserTypeDTO userTypeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Binding result error");
            return ResponseEntity.badRequest().build();
        }
        UserTypeDTO savedUserType = userTypeService.createUserType(userTypeDTO);
        log.debug("New user type is created : {}", userTypeDTO);
        return new ResponseEntity<>(savedUserType, HttpStatus.CREATED);

    }

    @PutMapping("/{id}")
    public ResponseEntity<UserTypeDTO> updateUserTypeById(@PathVariable Long id,
                                                          @RequestBody UserTypeDTO modifiedUserTypeDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Binding result error");
            return ResponseEntity.badRequest().build();
        }
        UserTypeDTO returnedUserTypeDTO = userTypeService.updateUserTypeById(modifiedUserTypeDTO, id);
        log.debug("User type with id: {} is now :{}", id, returnedUserTypeDTO);
        return new ResponseEntity<>(returnedUserTypeDTO, HttpStatus.ACCEPTED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserTypeDTO> deleteMovieTypeById(@PathVariable Long id) {
        log.info("Delete User Type by passing ID, where ID is:{}", id);
        userTypeService.deleteUserTypeById(id);
        log.debug("User type with id {} is deleted", id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}
