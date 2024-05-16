package com.desofs.backend.controllers;

import com.desofs.backend.domain.enums.Role;
import com.desofs.backend.dtos.AuthRequestDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.List;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@Tag(name = "Authentication")
@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtEncoder jwtEncoder;

    @Value("${jwt.exp-business-admin}")
    private Long expBusinessAdmin;

    @Value("${jwt.exp-property-owner}")
    private Long expPropertyOwner;

    @Value("${jwt.exp-customer}")
    private Long expCustomer;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid final AuthRequestDto request) throws BadCredentialsException {
        final Authentication authentication = this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = (User) authentication.getPrincipal();

        List<String> stringAuthorities = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        final long expiry;

        if (stringAuthorities.contains(Role.BusinessAdmin)) {
            expiry = expBusinessAdmin;
        } else if (stringAuthorities.contains(Role.PropertyOwner)) {
            expiry = expPropertyOwner;
        } else {
            expiry = expCustomer;
        }

        final String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(joining(" "));

        final JwtClaimsSet claims = JwtClaimsSet.builder()
                .expiresAt(Instant.now().plusSeconds(expiry))
                .subject(format("%s,%s", user.getUsername(), user.getPassword()))
                .claim("roles", scope)
                .claim("email", request.getEmail())
                .build();

        final String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).body(token);
    }
}
