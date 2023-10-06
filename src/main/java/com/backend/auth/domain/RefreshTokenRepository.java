package com.backend.auth.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
    Optional<RefreshToken> findByTokenValue(String tokenValue);

    Optional<RefreshToken> findByUid(String uid);
}
