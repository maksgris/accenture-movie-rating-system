package com.mgs.usertype.microservice.business.repository;

import com.mgs.library.business.repository.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTypeRepository extends JpaRepository<UserType, Long> {
    boolean existsByType(String type);
}
