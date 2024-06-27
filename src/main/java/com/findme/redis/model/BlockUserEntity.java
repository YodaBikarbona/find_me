package com.findme.redis.model;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.Instant;

@RedisHash("BlockUserEntity")
@Getter
@Setter
@NoArgsConstructor
public class BlockUserEntity implements Serializable {

    @Id
    private long id;
    private Instant createdAt;
    @Indexed
    private Long userId;
    @Indexed
    private String ipAddress;
    private long duration;

    public BlockUserEntity(long id, Long userId, String ipAddress) {
        this.id = id;
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.duration = 60;
        this.createdAt = Instant.now();
    }

}
