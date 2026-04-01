package com.amigoscode.carelio.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import com.amigoscode.carelio.user.entity.user.UserAddressInformation;
import com.amigoscode.carelio.user.repository.UserAddressInformationRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserAddressInformationService {

    private final UserAddressInformationRepository UserRepository;

    public List<UserAddressInformation> getAll() {
        return UserRepository.findAll();
    }

    public UserAddressInformation getById(Long id) {
        return UserRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserAddressInformation not found"));
    }

    public UserAddressInformation create(UserAddressInformation User) {
        return UserRepository.save(User);
    }

    public void delete(Long id) {
        UserRepository.deleteById(id);
    }
}
