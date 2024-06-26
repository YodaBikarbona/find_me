package com.findme.profile.repository;

import com.findme.profile.model.FollowingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowingRepository extends JpaRepository<FollowingEntity, Long> {

    @Query(value = "SELECT fe FROM FollowingEntity fe WHERE fe.follower.id = :followerId AND fe.following.id = :followingId")
    Optional<FollowingEntity> findByFollowingIdAndFollowerId(@Param("followingId") long followingId, @Param("followerId") long followerId);

    @Query(value = "SELECT fe FROM FollowingEntity fe WHERE fe.follower.id = :followerId")
    List<FollowingEntity> findAllByFollowerId(@Param("followerId") long followerId);

}
