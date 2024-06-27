package com.findme.redis.repository;

import com.findme.redis.model.UserRequestEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRequestRedisRepository extends CrudRepository<UserRequestEntity, Long> {

    Optional<UserRequestEntity> findByRequestId(@Param("requestId") String requestId);

    List<UserRequestEntity> findAllByUserId(@Param("userId") long userId);

    List<UserRequestEntity> findAllByIpAddress(@Param("ipAddress") String ipAddress);

}
