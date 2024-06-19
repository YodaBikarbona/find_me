package com.findme.config.filterconfig;

import com.findme.redis.dto.request.NewRequestLogDto;
import com.findme.redis.service.RedisService;
import com.findme.utils.ApplicationCtxHolderUtil;
import jakarta.servlet.*;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class RequestIdFilter implements Filter {

    public static final String REQUEST_ID_HEADER = "X-Request-ID";
    private final RedisService redisService;
    private static final Logger logger = LoggerFactory.getLogger(ApplicationCtxHolderUtil.class);

    public RequestIdFilter(RedisService redisService) {
        this.redisService = redisService;
    }

    @Override
    public void init(FilterConfig filterConfig) {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String requestId = httpRequest.getHeader(REQUEST_ID_HEADER);
        if (Objects.isNull(requestId) || requestId.isEmpty()) {
            requestId = UUID.randomUUID().toString();
        }
        httpRequest.setAttribute(REQUEST_ID_HEADER, requestId);
        chain.doFilter(request, response);
        redisService.newRequestLog(new NewRequestLogDto(redisService.generateUniqueId(), requestId, 0, httpRequest.getRemoteAddr(), httpRequest.getRequestURI()));
    }

    @Override
    public void destroy() { Filter.super.destroy(); }
}
