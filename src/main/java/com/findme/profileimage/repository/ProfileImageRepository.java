package com.findme.profileimage.repository;

import com.findme.profileimage.model.ProfileImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfileImageRepository extends JpaRepository<ProfileImageEntity, Long> {

    @Query("SELECT p_i " +
            "FROM ProfileImageEntity p_i " +
            "WHERE p_i.id NOT IN (" +
            "SELECT pi.id " +
            "FROM ProfileEntity p " +
            "LEFT JOIN ProfileImageEntity pi ON pi.id = p.profileImage.id " +
            "WHERE p.profileImage IS NOT NULL" +
            ")")
    List<ProfileImageEntity> findAllNotLinkedImages();

}
