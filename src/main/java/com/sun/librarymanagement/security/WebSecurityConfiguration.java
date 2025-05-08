package com.sun.librarymanagement.security;

import com.sun.librarymanagement.domain.model.UserRole;
import com.sun.librarymanagement.utils.ApiPaths;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@EnableScheduling
public class WebSecurityConfiguration {

    private final JwtAuthFilter jwtAuthFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth ->
                auth.requestMatchers(ApiPaths.USERS + "/**")
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.GET,
                        ApiPaths.PUBLISHERS,
                        ApiPaths.PUBLISHERS + "/*",
                        ApiPaths.AUTHORS,
                        ApiPaths.AUTHORS + "/*",
                        ApiPaths.CATEGORIES + "/**",
                        ApiPaths.BOOKS,
                        ApiPaths.BOOKS + "/*",
                        ApiPaths.COMMENTS
                    )
                    .permitAll()
                    .requestMatchers(
                        HttpMethod.POST,
                        ApiPaths.BOOKS + "/search"
                    )
                    .permitAll()
                    .requestMatchers(
                        ApiPaths.RATES,
                        ApiPaths.RATES + "/*"
                    ).hasRole(UserRole.USER.name())
                    .requestMatchers(ApiPaths.BASE_API_ADMIN + "/**").hasRole(UserRole.ADMIN.name())
                    .anyRequest()
                    .authenticated()
            )
            .exceptionHandling(exception ->
                exception.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .build();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
