package com.sun.librarymanagement.domain.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.librarymanagement.security.AppUserDetails;
import com.sun.librarymanagement.security.JwtAuthFilter;
import com.sun.librarymanagement.security.WebSecurityConfiguration;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@AutoConfigureMockMvc
@Import(WebSecurityConfiguration.class)
public abstract class BaseControllerTest {

    @MockitoBean
    protected JwtAuthFilter jwtAuthFilter;

    protected final ObjectMapper objectMapper = new ObjectMapper();

    @SneakyThrows
    @BeforeEach
    protected void setUp() {
        doAnswer(invocation -> {
            HttpServletRequest httpServletRequest = invocation.getArgument(0, HttpServletRequest.class);
            HttpServletResponse httpServletResponse = invocation.getArgument(1, HttpServletResponse.class);
            FilterChain chain = invocation.getArgument(2, FilterChain.class);
            chain.doFilter(httpServletRequest, httpServletResponse);
            return null;
        }).when(jwtAuthFilter).doFilter(any(), any(), any());
    }

    @SneakyThrows
    protected void mockSecurityContext(AppUserDetails userDetails) {
        doAnswer(invocation -> {
            HttpServletRequest httpServletRequest = invocation.getArgument(0, HttpServletRequest.class);
            HttpServletResponse httpServletResponse = invocation.getArgument(1, HttpServletResponse.class);
            FilterChain chain = invocation.getArgument(2, FilterChain.class);
            chain.doFilter(httpServletRequest, httpServletResponse);
            List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + userDetails.getRole()));
            UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            return null;
        }).when(jwtAuthFilter).doFilter(any(), any(), any());
    }
}
