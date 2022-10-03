package com.elos.vueauthspring.repository;

import com.elos.vueauthspring.models.ERole;
import com.elos.vueauthspring.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
