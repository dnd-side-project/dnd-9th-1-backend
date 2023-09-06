package com.backend.auth.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BlackListRepository extends CrudRepository<BlackList, String> {
    Optional<BlackList> findByAccessToken(String accessToken);

}
