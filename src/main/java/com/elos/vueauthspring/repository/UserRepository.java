package com.elos.vueauthspring.repository;

import java.util.Optional;

import com.elos.vueauthspring.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(@Param("username") String username);

    Optional<User> findByEmail(String email);

}
