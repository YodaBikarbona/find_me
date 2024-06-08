package com.findme.config.filterconfig;

import jakarta.servlet.*;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class RequestIdFilter implements Filter {

    public static final String REQUEST_ID_HEADER = "X-Request-ID";

    @Override
    public void init(FilterConfig filterConfig) {
        // Not used
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
    }

    @Override
    public void destroy() { Filter.super.destroy(); }
}
