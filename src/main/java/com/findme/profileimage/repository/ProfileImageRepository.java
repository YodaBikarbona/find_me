package com.findme.profileimage.repository;

import com.findme.profileimage.model.ProfileImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImageEntity, Long> {
}
