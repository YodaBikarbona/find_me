package com.findme.profile.repository;

import com.findme.profile.model.ProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository extends JpaRepository<ProfileEntity, Long> {

    Optional<ProfileEntity> findByUserId(@Param("userId") Long userId);
}
