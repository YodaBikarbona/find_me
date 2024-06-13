package com.findme.authorization;

import com.findme.exceptions.AuthorizationException;
import com.findme.user.model.UserEntity;
import com.findme.user.repository.UserRepository;
import com.findme.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;

    public AuthorizationInterceptor(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws AuthorizationException {
        if (handler instanceof HandlerMethod handlerMethod) {
            Authorization authorization = handlerMethod.getMethodAnnotation(Authorization.class);
            if (authorization != null) {
                String token = request.getHeader("Authorization");
                if (Objects.isNull(token) || token.isEmpty()) {
                    throw new AuthorizationException("Authorization token is missing!");
                }
                Claims claims = jwtUtil.getClaimsFromToken(token, Boolean.TRUE);
                Long userId = Long.parseLong(claims.get("id", String.class));
                UserEntity user = userRepository.findById(userId).orElseThrow(() -> new AuthorizationException("Invalid authorization!"));
                request.setAttribute("userId", userId);
            }
        }
        return true;
    }

}
