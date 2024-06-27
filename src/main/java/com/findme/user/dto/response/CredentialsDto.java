package com.findme.user.dto.response;

public record CredentialsDto(long id, Long profileId, String accessToken, String refreshToken) { }
