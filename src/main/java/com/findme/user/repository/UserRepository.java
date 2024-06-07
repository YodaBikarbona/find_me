package com.findme.user.repository;

import com.findme.user.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    UserEntity findByEmail(@Param("email") String email);

    Optional<UserEntity> findByUsername(@Param("username") String username);

}
