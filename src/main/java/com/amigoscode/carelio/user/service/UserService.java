package com.amigoscode.carelio.user.service;

import com.amigoscode.carelio.user.dto.CreateUserRequest;
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

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

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

    public User create(CreateUserRequest res)
    {
        if(userRepository.existsByEmail(res.getEmail()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email already exists");
        }
        if(userRepository.existsByUsername(res.getUsername()))
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username already exists");
        }

        User user = userMapper.toEntity(res);
        user.setUserStatus(UserStatus.ACTIVE);
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
}
