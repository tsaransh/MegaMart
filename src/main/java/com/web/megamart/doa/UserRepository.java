package com.web.megamart.doa;

import com.web.megamart.entity.UserDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserDetail, String> {

    Optional<UserDetail> findByEmailOrMobileNumber(String email, String mobileNumber);

    Optional<UserDetail> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);

    Optional<UserDetail> findByVerificationToken(String token);
}
