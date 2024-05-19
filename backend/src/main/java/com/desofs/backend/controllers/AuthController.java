package com.desofs.backend.controllers;

import com.desofs.backend.exceptions.DatabaseException;
import com.desofs.backend.domain.enums.Authority;
import com.desofs.backend.dtos.AuthRequestDto;
import com.desofs.backend.dtos.CreateUserDto;
import com.desofs.backend.dtos.FetchUserDto;
import com.desofs.backend.exceptions.NotAuthorizedException;
import com.desofs.backend.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

import static com.desofs.backend.config.UserDetailsConfig.hasAuthorization;
import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@Tag(name = "Authentication")
@RestController
@RequestMapping(path = "/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtEncoder jwtEncoder;

    private final UserService userService;

    @Value("${jwt.exp-business-admin}")
    private Long expBusinessAdmin;

    @Value("${jwt.exp-property-owner}")
    private Long expPropertyOwner;

    @Value("${jwt.exp-customer}")
    private Long expCustomer;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid final AuthRequestDto request)
            throws BadCredentialsException {

        final Authentication authentication = this.authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        User user = (User) authentication.getPrincipal();

        List<String> stringAuthorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).toList();
        final long expiry;

        if (stringAuthorities.contains(Authority.BUSINESSADMIN)) {
            expiry = expBusinessAdmin;
        } else if (stringAuthorities.contains(Authority.PROPERTYOWNER)) {
            expiry = expPropertyOwner;
        } else {
            expiry = expCustomer;
        }

        final String scope = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(joining(" "));

        final JwtClaimsSet claims = JwtClaimsSet.builder()
                .expiresAt(Instant.now().plusSeconds(expiry))
                .subject(format("%s", user.getUsername()))
                .claim("roles", scope)
                .claim("email", request.getEmail())
                .build();

        final String token = this.jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token).body(token);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FetchUserDto> register(@RequestBody CreateUserDto createUserDto,
                                                 Authentication authentication)
            throws DatabaseException, NotAuthorizedException {

        if (authentication != null
                && createUserDto.getRole().equals(Authority.BUSINESSADMIN)
                && !hasAuthorization(authentication, Authority.BUSINESSADMIN)) {
            throw new NotAuthorizedException("You're not authorized!");
        }

        FetchUserDto user = this.userService.create(createUserDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}