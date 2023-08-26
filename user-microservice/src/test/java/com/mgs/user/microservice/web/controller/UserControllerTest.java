package com.mgs.user.microservice.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgs.library.business.exceptions.ResourceAlreadyExists;
import com.mgs.library.business.exceptions.ResourceNotFoundException;
import com.mgs.library.model.UserDTO;
import com.mgs.user.microservice.business.service.UserService;
import com.mgs.user.microservice.test.data.UserTestData;
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

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {
    static String URL = "/api/v1/user";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;


    @Test
    @DisplayName("Test endpoint to find all users succesfully")
    void findAllInterns() throws Exception {
        List<UserDTO> listOfUserDto = UserTestData.createUserDtoListPredefined();
        when(userService.getAllUsers()).thenReturn(listOfUserDto);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(3)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value("testEmail@mail.com"))
                .andExpect(status().isOk());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    @DisplayName("Test endpoint to successfully finding a user by id")
    void findUserById() throws Exception {
        UserDTO userDTO = UserTestData.createUserDtoPredefined();
        when(userService.findUserById(anyLong())).thenReturn(Optional.of(userDTO));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1L))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("testEmail@mail.com"))
                .andExpect(status().isOk());
        verify(userService, times(1)).findUserById(1L);
    }

    @Test
    @DisplayName("Test endpoint to find a non existing user by id")
    void findNonExistingUserById() throws Exception {
        when(userService.findUserById(anyLong())).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL + "/1"))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(status().isNotFound());
        verify(userService, times(1)).findUserById(1L);
    }

    @Test
    @DisplayName("Test endpoint to find empty user list")
    void findAllUsersEmpty() throws Exception {
        when(userService.getAllUsers()).thenThrow(ResourceNotFoundException.class);
        mockMvc.perform(MockMvcRequestBuilders
                        .get(URL)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(status().isNotFound());
        verify(userService, times(1)).getAllUsers();

    }

    @Test
    @DisplayName("Test endpoint to create user")
    void createAUser() throws Exception {
        UserDTO userDTO = UserTestData.createUserDtoPredefined();
        userDTO.setId(null);

        when(userService.createUser(userDTO)).thenReturn(userDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(new ObjectMapper().writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("tester"));

        verify(userService, times(1)).createUser(userDTO);


    }

    @Test
    @DisplayName("Test endpoint to create a duplicate  user")
    void createAUserDuplicate() throws Exception {
        UserDTO userDTO = UserTestData.createUserDtoPredefined();
        userDTO.setId(null);

        when(userService.createUser(userDTO)).thenThrow(ResourceAlreadyExists.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .post(URL)
                        .content(new ObjectMapper().writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        verify(userService, times(1)).createUser(userDTO);
    }

    @Test
    @DisplayName("Test endpoint to update a user")
    void updateAUser() throws Exception {
        UserDTO userDTO = UserTestData.createUserDtoPredefined();
        when(userService.updateUser(userDTO, 1L)).thenReturn(userDTO);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(new ObjectMapper().writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("tester"));
        verify(userService, times(1)).updateUser(userDTO, 1L);
    }

    @Test
    @DisplayName("Test endpoint to update a user which does not exist")
    void updateANonExistingUser() throws Exception {
        UserDTO userDTO = UserTestData.createUserDtoPredefined();
        when(userService.updateUser(userDTO, 1L)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(MockMvcRequestBuilders
                        .put(URL + "/1")
                        .content(new ObjectMapper().writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ResourceNotFoundException))
                .andExpect(status().isNotFound());
        verify(userService, times(1)).updateUser(userDTO, 1L);
    }

    @Test
    @DisplayName("Test endpoint to successfully deleting a user by id")
    void deleteUserById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
        verify(userService, times(1)).deleteUserById(1L);
    }

}
