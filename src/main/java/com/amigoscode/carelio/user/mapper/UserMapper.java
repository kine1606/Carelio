package com.amigoscode.carelio.user.mapper;

import com.amigoscode.carelio.user.dto.CreateUserRequest;
import com.amigoscode.carelio.user.dto.UpdateUserRequest;
import com.amigoscode.carelio.user.dto.UserResponse;
import com.amigoscode.carelio.user.entity.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")


public interface UserMapper
{
    User toEntity(CreateUserRequest createUserRequest);
    UserResponse toResponse(User user);
    List<UserResponse> toResponseList(List<User> user);

    void updateEntity(UpdateUserRequest request, @MappingTarget User entity);
}