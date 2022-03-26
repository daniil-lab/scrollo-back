package com.inst.base.repository.user;

import com.inst.base.entity.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByLogin(String login);

    Optional<User> findByEmailDataEmail(String email);

    Optional<User> findByPhoneDataPhone(String phone);

    Optional<User> findByIdAndFollowedId(UUID userId, UUID followedId);

    Page<User> findByLoginContainingOrPersonalInformationNameContaining(String username, String name, Pageable pageable);
}
