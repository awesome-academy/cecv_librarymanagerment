package com.sun.librarymanagement.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends GenericFilter {

    public static final String TOKEN_PREFIX = "Token ";

    private final JwtUtils jwtUtils;
    private final AuthenticationProvider authenticationProvider;

    @Override
    public void doFilter(
        ServletRequest request,
        ServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {
        Optional.ofNullable(((HttpServletRequest) request).getHeader(HttpHeaders.AUTHORIZATION))
            .filter(header -> header.startsWith(TOKEN_PREFIX))
            .map(header -> header.substring(TOKEN_PREFIX.length()))
            .filter(jwtUtils::validateToken)
            .map(jwtUtils::getSub)
            .map(authenticationProvider::getAuthentication)
            .ifPresent(SecurityContextHolder.getContext()::setAuthentication);
        chain.doFilter(request, response);
    }
}
