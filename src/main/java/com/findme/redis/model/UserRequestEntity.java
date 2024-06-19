package com.findme.redis.model;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.Instant;

@RedisHash("UserRequestEntity")
@Getter
@Setter
public class UserRequestEntity implements Serializable {

    @Id
    private long id;
    private String requestId;
    private Instant createdAt;
    private long userId;
    private String ipAddress;
    private String route;

    public UserRequestEntity(long id, String requestId, long userId, String ipAddress, String route) {
        this.id = id;
        this.requestId = requestId;
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.route = route;
        this.createdAt = Instant.now();
    }
}
