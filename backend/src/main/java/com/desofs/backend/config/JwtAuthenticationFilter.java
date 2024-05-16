package com.desofs.backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNullApi;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Date;
import java.util.Objects;

@RequiredArgsConstructor
@Configuration
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;

    private final JwtDecoder jwtDecoder;

    private boolean isTokenValid(Jwt token) {
        return token != null && token.getExpiresAt() != null && token.getExpiresAt().isAfter(new Date().toInstant());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            if (request.getHeader("Authorization") != null) {
                String jwtTokenRaw = request.getHeader("Authorization").substring(7);
                Jwt jwtToken = jwtDecoder.decode(jwtTokenRaw);

                if (isTokenValid(jwtToken)) {

                    if (jwtToken.getClaimAsString("email") != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(jwtToken.getClaimAsString("email"));
                        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                                null, userDetails.getAuthorities());

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error on JwtAuthenticationFilter {}", e.getMessage());
        } finally {
            filterChain.doFilter(request, response);
        }
    }

}
