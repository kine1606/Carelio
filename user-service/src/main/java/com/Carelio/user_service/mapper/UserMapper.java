package com.Carelio.user_service.mapper;


import com.Carelio.user_service.dto.response.UserResponse;
import com.Carelio.user_service.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper
{
    UserResponse toResponse(User user);
}
