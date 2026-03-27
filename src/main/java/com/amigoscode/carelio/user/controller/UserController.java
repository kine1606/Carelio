package com.amigoscode.carelio.user.controller;

import com.amigoscode.carelio.user.dto.CreateUserRequest;
import com.amigoscode.carelio.user.dto.UserResponse;
import com.amigoscode.carelio.user.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.amigoscode.carelio.user.entity.user.User;
import com.amigoscode.carelio.user.service.UserService;
import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController
{

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserResponse> getAll()
    {
        return userMapper.toResponseList(userService.getAll());
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Long id)
    {
        return userService.getById(id);
    }

    @PostMapping
    public UserResponse create(@RequestBody CreateUserRequest request)
    {
        return userMapper.toResponse(userService.create(request));
    }

    @DeleteMapping("/{id}")
    public UserResponse delete(@PathVariable Long id)
    {
        return userMapper.toResponse(userService.softDelete(id));
    }
}
