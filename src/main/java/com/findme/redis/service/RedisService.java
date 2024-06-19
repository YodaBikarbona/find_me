package com.findme.redis.service;

import com.findme.redis.dto.request.NewRequestLogDto;
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

@Service
@RequiredArgsConstructor
public class RedisService {

    private final BlockUserRedisRepository blockUserRedisRepository;
    private final UserRequestRedisRepository userRequestRedisRepository;
    private final RedisTemplate<String, String> redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);


    @Transactional
    public void newRequestLog(NewRequestLogDto newRequestLogDto) {
        try {
            UserRequestEntity userRequestEntity = new UserRequestEntity(newRequestLogDto.id(), newRequestLogDto.requestId(), newRequestLogDto.userId(), newRequestLogDto.ipAddress(), newRequestLogDto.route());
            userRequestRedisRepository.save(userRequestEntity);
        } catch (Exception ex) {
            logger.error("The new request log failed.", ex);
        }
    }

    public int countRequestLogByUserId(long userId) {
        List<UserRequestEntity> userRequestEntities = userRequestRedisRepository.findAllByUserId(userId);
        return userRequestEntities.size();
    }

    public long generateUniqueId() {
        return redisTemplate.opsForValue().increment("user_request_entity_id", 1);
    }

}
