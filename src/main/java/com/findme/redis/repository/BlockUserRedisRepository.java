package com.findme.redis.repository;

import com.findme.redis.model.BlockUserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockUserRedisRepository extends CrudRepository<BlockUserEntity, Long> {
}
