package com.findme.authorization;

import com.findme.exceptions.AuthorizationException;
import com.findme.exceptions.NoPermissionException;
import com.findme.redis.dto.request.NewRequestLogDto;
import com.findme.redis.model.UserRequestEntity;
import com.findme.redis.repository.UserRequestRedisRepository;
import com.findme.redis.service.RedisService;
import com.findme.user.model.UserEntity;
import com.findme.user.repository.UserRepository;
import com.findme.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;
    private final UserRequestRedisRepository redisRepository;
    private final UserRequestRedisRepository userRequestRedisRepository;
    private final RedisService redisService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws AuthorizationException {
        NewRequestLogDto newRequestLogDto = new NewRequestLogDto(redisService.generateUniqueId(), request.getAttribute("X-Request-ID").toString(), 0, request.getRemoteAddr(), request.getRequestURI());
        if (handler instanceof HandlerMethod handlerMethod) {
            Authorization authorization = handlerMethod.getMethodAnnotation(Authorization.class);
            RefreshAuthorization refreshAuthorization = handlerMethod.getMethodAnnotation(RefreshAuthorization.class);
            if (!Objects.isNull(authorization) || !Objects.isNull(refreshAuthorization)) {
                String token = request.getHeader("Authorization");
                if (Objects.isNull(token) || token.isEmpty()) {
                    throw new AuthorizationException("Authorization token is missing!");
                }
                Claims claims;
                if (!Objects.isNull(authorization)) {
                    claims = jwtUtil.getClaimsFromToken(token, Boolean.TRUE);
                } else {
                    claims = jwtUtil.getClaimsFromToken(token, Boolean.FALSE);
                }
                Long userId = Long.parseLong(claims.get("id", String.class));
                newRequestLogDto.userId();
                Optional<UserRequestEntity> userRequestEntity = userRequestRedisRepository.findByRequestId(request.getAttribute("X-Request-ID").toString());
                if (userRequestEntity.isEmpty()) {
                    throw new NoPermissionException("You don't have a permission!");
                }
                userRequestEntity.get().setUserId(userId);
                userRequestRedisRepository.save(userRequestEntity.get());
                UserEntity user = userRepository.findById(userId).orElseThrow(() -> new AuthorizationException("Invalid credentials!"));
                request.setAttribute("userId", userId);
            }
        }
        return true;
    }

    private void saveLoginLog(NewRequestLogDto newRequestLogDto) {
        if (Objects.equals(newRequestLogDto.userId(), 0)) {
            throw new NoPermissionException("You don't have a permission!");
        }
        redisService.newRequestLog(newRequestLogDto);
    }

}
