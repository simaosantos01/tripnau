package com.desofs.backend.controllers;

import com.desofs.backend.database.DatabaseException;
import com.desofs.backend.dtos.CreateUserDto;
import com.desofs.backend.dtos.FetchUserDto;
import com.desofs.backend.services.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User", description = "Endpoints for managing users")
@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userEmail}")
    public ResponseEntity<FetchUserDto> getUserByEmail(@PathVariable final String userEmail) {
        FetchUserDto user = this.userService.findByEmail(userEmail);
        return ResponseEntity.ok().body(user);
    }

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<FetchUserDto> create(@RequestBody CreateUserDto createUserDto) throws DatabaseException {
        FetchUserDto user = this.userService.create(createUserDto);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
