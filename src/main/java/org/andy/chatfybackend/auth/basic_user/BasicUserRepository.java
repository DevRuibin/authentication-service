package org.andy.chatfybackend.auth.basic_user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BasicUserRepository extends JpaRepository<BasicUser, Long> {
    Optional<BasicUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
