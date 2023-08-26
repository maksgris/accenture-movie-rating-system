package com.mgs.usertype.microservice.business.service.impl;

import com.mgs.library.business.exceptions.ResourceAlreadyExists;
import com.mgs.library.business.exceptions.ResourceNotFoundException;
import com.mgs.library.business.mappers.UserTypeMapper;
import com.mgs.library.business.repository.model.UserType;
import com.mgs.library.model.UserTypeDTO;
import com.mgs.usertype.microservice.business.repository.UserTypeRepository;
import com.mgs.usertype.microservice.test.data.UserTypeTestData;
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

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@SuppressWarnings("squid:S5778")
class UserTypeServiceImplTest {

    @Mock
    private UserTypeRepository userTypeRepository;

    @Autowired
    private UserTypeMapper userTypeMapper;
    @Mock
    private UserTypeMapper mockUserTypeMapper;

    @Spy
    @InjectMocks
    private UserTypeServiceImpl userTypeService;

    private UserTypeDTO userTypeDTO;
    private UserType userType;

    @BeforeEach
    void createTestData() {
        this.userTypeDTO = UserTypeTestData.createUserTypeDto();
        this.userType = userTypeMapper.mapUserTypeDtoToUserType(userTypeDTO);
    }

    @Test
    @DisplayName("Retrieval of all UserTypes")
    void testUserTypesSuccessfully() {
        List<UserTypeDTO> userTypeDtoList = UserTypeTestData.createUserTypeDtoList();
        List<UserType> movieTypeList = userTypeMapper.mapUserTypeDtoListToUserTypeList(userTypeDtoList);
        when(userTypeRepository.findAll()).thenReturn(movieTypeList);
        when(mockUserTypeMapper.mapUserTypeListToUserTypeListDto(movieTypeList)).thenReturn(userTypeDtoList);
        List<UserTypeDTO> movieTypeDtoListReturned = userTypeService.getAllUserTypes();
        Assertions.assertEquals(userTypeDtoList, movieTypeDtoListReturned);
        verify(userTypeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Retrieval of empty UserType list")
    void testGetAllUserTypeEmpty() {
        List<UserTypeDTO> emptyUserTypeDTOList = new ArrayList<UserTypeDTO>();
        List<UserType> emptyUserList = userTypeMapper.mapUserTypeDtoListToUserTypeList(emptyUserTypeDTOList);
        when(userTypeRepository.findAll()).thenReturn(emptyUserList);
        when(mockUserTypeMapper.mapUserTypeListToUserTypeListDto(emptyUserList)).thenReturn(emptyUserTypeDTOList);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userTypeService.getAllUserTypes());
        verify(userTypeRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Finding UserType by id")
    void testSuccessfullyFindingUserTypeById() {

        when(mockUserTypeMapper.mapUserTypeToUserTypeDto(userType)).thenReturn(userTypeDTO);
        when(userTypeRepository.findById(anyLong())).thenReturn(Optional.of(userType));
        userTypeService.findUserTypeById(anyLong());
        verify(userTypeRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("Find non existing UserType by id")
    void testFailingFindUserTypeById() {
        when(userTypeRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userTypeService.findUserTypeById(anyLong()));
    }

    @Test
    @DisplayName("Deleting UserType by id")
    void testDeletingUserTypeById() {
        doReturn(Optional.of(userTypeDTO)).when(userTypeService).findUserTypeById(1L);
        userTypeService.deleteUserTypeById(1L);
        verify(userTypeRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Deleting when UserType does not exist")
    void testDeleteUserTypeNotFound() {
        doReturn(Optional.empty()).when(userTypeService).findUserTypeById(1L);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userTypeService.deleteUserTypeById(1L));
    }


    @Test
    @DisplayName("Create a UserType")
    void testSuccessfullyCreatingAUserType() {
        when(userTypeRepository.existsByType(userTypeDTO.getType())).thenReturn(false);
        when(mockUserTypeMapper.mapUserTypeDtoToUserType(userTypeDTO)).thenReturn(userType);
        when(userTypeRepository.save(userType)).thenReturn(userType);
        userTypeService.createUserType(userTypeDTO);
        verify(userTypeRepository, times(1)).save(userType);
    }

    @Test
    @DisplayName("Create a duplicate UserType")
    void testFailingCreatingAUserType() {
        when(userTypeRepository.existsByType(anyString())).thenReturn(true);
        Assertions.assertThrows(ResourceAlreadyExists.class, () -> userTypeService.createUserType(userTypeDTO));
    }

    @Test
    @DisplayName("Update a UserType by Id")
    void testSuccessfullyUpdatingAUserTypeById() {

        when(userTypeRepository.existsById(anyLong())).thenReturn(true);
        when(userTypeRepository.existsByType(anyString())).thenReturn(false);
        when(mockUserTypeMapper.mapUserTypeDtoToUserType(any(UserTypeDTO.class))).thenReturn(userType);
        when(mockUserTypeMapper.mapUserTypeToUserTypeDto(any(UserType.class))).thenReturn(userTypeDTO);
        when(userTypeRepository.save(userType)).thenReturn(userType);
        UserTypeDTO returnedUserTypeDto = userTypeService.updateUserTypeById(userTypeDTO, 1L);
        Assertions.assertEquals(userTypeDTO, returnedUserTypeDto);
        verify(userTypeRepository, times(1)).save(userType);

    }

    @Test
    @DisplayName("Update UserType which does not exist")
    void testFailUpdatingNonExistingUserType() {
        when(userTypeRepository.existsById(anyLong())).thenReturn(false);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userTypeService.updateUserTypeById(userTypeDTO, 1L));
    }

    @Test
    void testFailUpdatingAUserTypeWhichDoesNotExist() {
        when(userTypeRepository.existsById(anyLong())).thenReturn(false);
        when(userTypeRepository.existsByType(anyString())).thenReturn(true);
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userTypeService.updateUserTypeById(userTypeDTO, 1L));
    }

    @Test
    @DisplayName("Delete a UserType")
    void testSuccessfullyDeleteAUserType() {
        doReturn(Optional.of(userTypeDTO)).when(userTypeService).findUserTypeById(anyLong());
        userTypeService.deleteUserTypeById(1L);
        verify(userTypeRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Delete a non existing UserType")
    void testFailingDeleteAUserType() {
        doReturn(Optional.empty()).when(userTypeService).findUserTypeById(anyLong());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> userTypeService.deleteUserTypeById(1L));
    }
}
