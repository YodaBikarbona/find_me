package com.findme.post.repository;

import com.findme.post.model.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query(
            value = "SELECT " +
                    "*, " +
                    "(6371 * acos(cos(radians(:latitude)) * cos(radians(latitude)) * cos(radians(longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(latitude)))) AS distance\n" +
                    "FROM posts\n" +
                    "WHERE profile_id != :profile_id\n" +
                    "ORDER BY distance\n" +
                    "LIMIT :limit", nativeQuery = true
    )
    List<PostEntity> findNearestPosts(@Param("profile_id") long profileId, @Param("longitude") float longitude,@Param("latitude") float latitude, @Param("limit") int limit);

    @Query(
            value = "SELECT *\n" +
                    "FROM (\n" +
                    "    SELECT *,\n" +
                    "           (6371 * acos(cos(radians(:latitude)) * cos(radians(latitude)) * cos(radians(longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(latitude)))) AS distance\n" +
                    "    FROM posts\n" +
                    "    WHERE profile_id != :profile_id\n" +
                    ") AS subquery\n" +
                    "WHERE distance < :radius\n" +
                    "ORDER BY distance\n" +
                    "LIMIT :limit",
            nativeQuery = true
    )
    List<PostEntity> findNearestPostsWithinRadius(@Param("profile_id") long profileId, @Param("longitude") float longitude,@Param("latitude") float latitude,@Param("radius") int radius, @Param("limit") int limit);

}
