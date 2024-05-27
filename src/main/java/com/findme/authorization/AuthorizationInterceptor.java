package com.findme.authorization;

import com.findme.exceptions.AuthorizationException;
import com.findme.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;

    public AuthorizationInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        if (handler instanceof HandlerMethod handlerMethod) {
            Authorization authorization = handlerMethod.getMethodAnnotation(Authorization.class);
            if (authorization != null) {
                String token = request.getHeader("Authorization");
                if (token == null || token.isEmpty()) {
                    throw new AuthorizationException("Authorization token is missing!");
                }
                Claims claims = jwtUtil.getClaimsFromToken(token, Boolean.TRUE);
                Long userId = Long.parseLong(claims.get("id", String.class));
                request.setAttribute("userId", userId);
            }
        }
        return true;
    }

}
