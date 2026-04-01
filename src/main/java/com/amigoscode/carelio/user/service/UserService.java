package com.amigoscode.carelio.user.service;

import com.amigoscode.carelio.user.dto.CreateUserRequest;
import com.amigoscode.carelio.user.dto.UpdateUserRequest;
import com.amigoscode.carelio.user.entity.user.UserStatus;
import com.amigoscode.carelio.user.mapper.UserMapper;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.amigoscode.carelio.user.entity.user.User;
import com.amigoscode.carelio.user.repository.UserRepository;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService
{

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<User> getAll()
    {
        return userRepository.findAllByDeletedFalse();
    }

    public User getById(Long id)
    {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Transactional
    public User create(CreateUserRequest res)
    {
        if (userRepository.existsByEmail(res.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email already exists");
        }
        if (userRepository.existsByUsername(res.getUsername())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already exists");
        }
        if (userRepository.existsByPhoneNumber(res.getPhoneNumber())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "phone number already exists");
        }
        User user = userMapper.toEntity(res);
        user.setUserStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    @Transactional
    public User softDelete(Long id)
    {
        User user = getById(id);
        user.setUserStatus(UserStatus.INACTIVE);
        user.setDeleted(true);
        return userRepository.save(user);
    }

    @Transactional
    public User update(Long id, UpdateUserRequest res)
    {
        User u = getById(id);

        if(res.getAvtUrl() != null) u.setAvtUrl(res.getAvtUrl());
        if(res.getPhoneNumber() != null) u.setPhoneNumber(res.getPhoneNumber());
        if(res.getUsername() != null) u.setUsername(res.getUsername());
        if(res.getFullName() != null) u.setFullName(res.getFullName());

        return userRepository.save(u);
    }
}
