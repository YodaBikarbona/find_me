package com.findme.redis.model;

import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.Instant;

@RedisHash("UserRequestEntity")
@Getter
@Setter
@NoArgsConstructor
public class UserRequestEntity implements Serializable {

    @Id
    private long id;
    @Indexed
    private String requestId;
    private Instant createdAt;
    @Indexed
    private Long userId;
    @Indexed
    private String ipAddress;
    private String route;

    public UserRequestEntity(long id, String requestId, Long userId, String ipAddress, String route) {
        this.id = id;
        this.requestId = requestId;
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.route = route;
        this.createdAt = Instant.now();
    }
}
