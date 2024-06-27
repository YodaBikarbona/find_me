package com.findme.authorization;

import com.findme.exceptions.AuthorizationException;
import com.findme.exceptions.NoPermissionException;
import com.findme.exceptions.NotFoundException;
import com.findme.exceptions.ToManyRequestsException;
import com.findme.redis.dto.request.RedisDto;
import com.findme.redis.model.UserRequestEntity;
import com.findme.redis.repository.BlockUserRedisRepository;
import com.findme.redis.repository.UserRequestRedisRepository;
import com.findme.redis.service.RedisService;
import com.findme.user.model.UserEntity;
import com.findme.user.repository.UserRepository;
import com.findme.utils.ApplicationCtxHolderUtil;
import com.findme.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final BlockUserRedisRepository blockUserRedisRepository;
    private final UserRequestRedisRepository userRequestRedisRepository;
    private final RedisService redisService;
    public static final String REQUEST_ID_HEADER = "X-Request-ID";
    private final int maxRequests = 50;
    private static final Logger logger = LoggerFactory.getLogger(ApplicationCtxHolderUtil.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws AuthorizationException {
        boolean hasToken = Boolean.FALSE;
        Long userId = null;
        if (handler instanceof HandlerMethod handlerMethod) {
            Authorization authorization = handlerMethod.getMethodAnnotation(Authorization.class);
            RefreshAuthorization refreshAuthorization = handlerMethod.getMethodAnnotation(RefreshAuthorization.class);
            if (!Objects.isNull(authorization) || !Objects.isNull(refreshAuthorization)) {
                String token = request.getHeader("Authorization");
                if (Objects.isNull(token) || token.isEmpty()) {
                    throw new AuthorizationException("Authorization token is missing!");
                }
                hasToken = Boolean.TRUE;
                Claims claims;
                if (!Objects.isNull(authorization)) {
                    claims = jwtUtil.getClaimsFromToken(token, Boolean.TRUE);
                } else {
                    claims = jwtUtil.getClaimsFromToken(token, Boolean.FALSE);
                }
                userId = Long.parseLong(claims.get("id", String.class));
                if (redisService.getBlockUserEntity(null, userId).isPresent()) {
                    throw new NotFoundException("To many requests!");

                }
                logger.info("---------- Searching by RequestId: {}", request.getAttribute(REQUEST_ID_HEADER).toString());
                Optional<UserRequestEntity> userRequestEntity = userRequestRedisRepository.findByRequestId(request.getAttribute(REQUEST_ID_HEADER).toString());
                if (userRequestEntity.isEmpty()) {
                    throw new NoPermissionException("You don't have a permission!");
                }
                userRequestEntity.get().setUserId(userId);
                userRequestRedisRepository.save(userRequestEntity.get());
                int records = redisService.countRequestLogByUserId(userId);
                if (records == maxRequests) {
                    throw new NotFoundException("To many requests!");
                }
                UserEntity user = userRepository.findById(userId).orElseThrow(() -> new AuthorizationException("Invalid credentials!"));
                request.setAttribute("userId", userId);
            }
        }
        if (redisService.getBlockUserEntity(request.getRemoteAddr(), userId).isPresent()) {
            throw new NotFoundException("To many requests!");
        }
        if (!hasToken) {
            int records = redisService.countRequestLogByIpAddress(request.getRemoteAddr());
            if (records >= maxRequests) {
                redisService.blockUser(new RedisDto(redisService.generateUniqueIdBlockUserEntity(), request.getRequestId(), userId, request.getRemoteAddr(), request.getRequestURI()));
                throw new ToManyRequestsException("To many requests!");
            }
        }
        return true;
    }

}
