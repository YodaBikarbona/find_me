package com.findme.redis.dto.request;

public record NewRequestLogDto(long id, String requestId, long userId, String ipAddress, String route) {
}
