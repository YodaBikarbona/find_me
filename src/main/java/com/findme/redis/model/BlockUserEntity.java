package com.findme.redis.model;

import jakarta.persistence.Id;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.Instant;

@RedisHash("BlockUserEntity")
@Getter
public class BlockUserEntity implements Serializable {

    @Id
    private long id;
    private final Instant createdAt;
    private final long userId;
    private final long duration;

    public BlockUserEntity(long userId, long duration) {
        this.userId = userId;
        this.duration = duration;
        this.createdAt = Instant.now();
    }

}
