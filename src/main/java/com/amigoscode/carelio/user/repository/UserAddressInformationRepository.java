package com.amigoscode.carelio.user.repository;

import com.amigoscode.carelio.user.entity.user.UserAddressInformation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAddressInformationRepository extends JpaRepository<UserAddressInformation, Long> { }
