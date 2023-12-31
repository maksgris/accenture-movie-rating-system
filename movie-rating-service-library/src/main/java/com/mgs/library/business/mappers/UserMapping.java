package com.mgs.library.business.mappers;

import com.mgs.library.business.repository.model.User;
import com.mgs.library.business.repository.model.UserType;
import com.mgs.library.model.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapping {
    @Mapping(source = "userType", target = "userType", qualifiedByName = "userTypeToString")
    UserDTO mapUserToUserDto(User userEntity);

    @Mapping(source = "userType", target = "userType", qualifiedByName = "userTypeStringToUserTypeDto")
    User mapUserDtoToUser(UserDTO userDto);

    List<UserDTO> mapUserListToUserDto(List<User> userEntities);

    List<User> mapUserListDtoToUserList(List<UserDTO> userEntities);

    @Named("userTypeToString")
    default String userUserTypeToUserTypeDto(UserType userType) {
        return userType.getType();
    }

    @Named("userTypeStringToUserTypeDto")
    default UserType userUserTypeToUserTypeDto(String userType) {
        return new UserType(userType);
    }

}
