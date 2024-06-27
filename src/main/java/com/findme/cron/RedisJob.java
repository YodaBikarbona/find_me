package com.findme.cron;

import com.findme.redis.model.BlockUserEntity;
import com.findme.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RedisJob implements Job {

    private final RedisService redisService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        List<BlockUserEntity> blockUserEntities = redisService.getAllBlockUserEntities();
        for (BlockUserEntity blockUserEntity : blockUserEntities) {
            redisService.removeBlockedUser(blockUserEntity);
            if (Instant.now().isAfter(Instant.now().plus(Duration.ofMinutes(blockUserEntity.getDuration())))) {
                redisService.removeBlockedUser(blockUserEntity);
            }
        }
        redisService.cleanUserRequestLog();
    }

}
