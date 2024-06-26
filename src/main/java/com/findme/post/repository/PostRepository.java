package com.findme.post.repository;

import com.findme.post.model.PostEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, Long> {

    @Query(
            value = "SELECT " +
                    "id, longitude, latitude, " +
                    "(6371 * acos(cos(radians(:latitude)) * cos(radians(latitude)) * cos(radians(longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(latitude)))) AS distance\n" +
                    "FROM posts\n" +
                    "WHERE profile_id != :profile_id\n" +
                    "ORDER BY distance\n" +
                    "LIMIT :limit", nativeQuery = true
    )
    List<Object[]> findNearestPosts(@Param("profile_id") long profileId, @Param("longitude") float longitude,@Param("latitude") float latitude, @Param("limit") int limit);

    @Query(
            value = "SELECT " +
                    "id, longitude, latitude, " +
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
    List<Object[]> findNearestPostsWithinRadius(@Param("profile_id") long profileId, @Param("longitude") float longitude,@Param("latitude") float latitude,@Param("radius") int radius, @Param("limit") int limit);

    @Query(
            value = "SELECT pe FROM PostEntity pe WHERE pe.profile.id IN :profile_ids ORDER BY pe.createdAt DESC"
    )
    List<PostEntity> findPosts(@Param("profile_ids") List<Long> profileIds, Pageable pageable);

    @Query("SELECT COUNT(pe) FROM PostEntity pe WHERE pe.profile.id = :profileId AND pe.createdAt BETWEEN :startDate AND :endDate")
    long countByCreatedAtBetween(long profileId, Instant startDate, Instant endDate);

    @Query(
            value = "SELECT " +
                    "(6371 * acos(cos(radians(:latitude)) * cos(radians(latitude)) * cos(radians(longitude) - radians(:longitude)) + sin(radians(:latitude)) * sin(radians(latitude)))) AS distance\n" +
                    "FROM posts\n" +
                    "WHERE id = :id\n", nativeQuery = true
    )
    double findPostDistance(@Param("id") long id, @Param("longitude") float longitude, @Param("latitude") float latitude);

}
