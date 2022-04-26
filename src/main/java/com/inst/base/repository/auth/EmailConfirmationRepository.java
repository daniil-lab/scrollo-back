package com.inst.base.repository.auth;

import com.inst.base.entity.auth.ConfirmationState;
import com.inst.base.entity.auth.EmailConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailConfirmationRepository extends JpaRepository<EmailConfirmation, UUID> {
    Optional<EmailConfirmation> findByEmailAndState(String email, ConfirmationState state);

    Optional<EmailConfirmation> findByIdAndState(UUID id, ConfirmationState state);
}
