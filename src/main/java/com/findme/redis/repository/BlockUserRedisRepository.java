package com.findme.redis.repository;

import com.findme.redis.model.BlockUserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockUserRedisRepository extends CrudRepository<BlockUserEntity, Long> {

    Optional<BlockUserEntity> findByIpAddress(@Param("ipAddress") String ipAddress);

    Optional<BlockUserEntity> findByUserId(@Param("userId") long userId);

}
