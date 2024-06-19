package com.findme.redis.service;

import com.findme.redis.dto.request.RedisDto;
import com.findme.redis.model.BlockUserEntity;
import com.findme.redis.model.UserRequestEntity;
import com.findme.redis.repository.BlockUserRedisRepository;
import com.findme.redis.repository.UserRequestRedisRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final BlockUserRedisRepository blockUserRedisRepository;
    private final UserRequestRedisRepository userRequestRedisRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);


    @Transactional
    public void newRequestLog(RedisDto newRequestLogDto) {
        try {
            UserRequestEntity userRequestEntity = new UserRequestEntity(newRequestLogDto.getId(), newRequestLogDto.getRequestId(), newRequestLogDto.getUserId(), newRequestLogDto.getIpAddress(), newRequestLogDto.getRoute());
            userRequestRedisRepository.save(userRequestEntity);
        } catch (Exception ex) {
            logger.error("The new request log failed.", ex);
        }
    }

    @Transactional
    public void blockUser(RedisDto blockUserDto) {
        try {
            BlockUserEntity blockUserEntity = new BlockUserEntity(blockUserDto.getId(), blockUserDto.getUserId(), blockUserDto.getIpAddress());
            blockUserRedisRepository.save(blockUserEntity);
        } catch (Exception ex) {
            logger.error("The block user failed.", ex);
        }
    }

    public int countRequestLogByUserId(long userId) {
        List<UserRequestEntity> userRequestEntities = userRequestRedisRepository.findAllByUserId(userId);
        return userRequestEntities.size();
    }

    public long generateUniqueId() {
        return redisTemplate.opsForValue().increment("user_request_entity_id", 1);
    }

    public long generateUniqueIdBlockUserEntity() {
        return redisTemplate.opsForValue().increment("block_user_entity_id", 1);
    }

    public int countRequestLogByIpAddress(String ipAddress) {
        List<UserRequestEntity> userRequestEntities = userRequestRedisRepository.findAllByIpAddress(ipAddress);
        return userRequestEntities.size();
    }

    public Optional<BlockUserEntity> getBlockUserEntity(String ipAddress, Long userId) {
        if (Objects.isNull(userId)) {
            return blockUserRedisRepository.findByIpAddress(ipAddress);
        }
        return blockUserRedisRepository.findByUserId(userId);
    }

}
