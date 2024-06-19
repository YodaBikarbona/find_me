package com.findme.redis.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
public class RedisDto {

    private final long id;
    private final String requestId;
    @Setter
    private Long userId;
    private final String ipAddress;
    private final String route;

    public RedisDto(long id, String requestId, Long userId, String ipAddress, String route) {
        this.id = id;
        this.requestId = requestId;
        this.userId = userId;
        this.ipAddress = ipAddress;
        this.route = route;
    }

}
