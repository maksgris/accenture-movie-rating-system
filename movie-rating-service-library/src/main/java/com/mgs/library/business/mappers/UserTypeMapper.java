package com.mgs.library.business.mappers;

import com.mgs.library.business.repository.model.User;
import com.mgs.library.business.repository.model.UserType;
import com.mgs.library.model.UserTypeDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface UserTypeMapper {

    @Mapping(source = "users", target = "userIds", qualifiedByName = "usersToUsersIdLong")
    UserTypeDTO mapUserTypeToUserTypeDto(UserType userTypeEntity);

    @Mapping(source = "userIds", target = "users", qualifiedByName = "userIdLongToUser")
    UserType mapUserTypeDtoToUserType(UserTypeDTO userTypeDto);

    List<UserTypeDTO> mapUserTypeListToUserTypeListDto(List<UserType> userTypeEntities);

    List<UserType> mapUserTypeDtoListToUserTypeList(List<UserTypeDTO> userTypeEntities);

    @Named("usersToUsersIdLong")
    default List<Long> usersToUsersIdLong(List<User> listOfUsers) {
        return listOfUsers.stream()
                .map(User::getId).collect(Collectors.toList());
    }

    @Named("userIdLongToUser")
    default List<User> userIdLongToUser(List<Long> listOfUserIds) {
        if (listOfUserIds == null)
            return new ArrayList<>();
        return listOfUserIds.stream()
                .map(User::new).collect(Collectors.toList());
    }
}
