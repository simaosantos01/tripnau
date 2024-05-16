package com.desofs.backend.domain.aggregates;

import com.desofs.backend.domain.enums.Role;
import com.desofs.backend.domain.valueobjects.*;
import com.desofs.backend.dtos.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserDomainTest {
    @Test
    @DisplayName("Test constructor with valid UserDto")
    void testConstructorValidUserDto() {
        UserDto userDto = new UserDto("123456", "John Doe", "john.doe@example.com", "Abcdef123!@#", Role.Customer, true);

        UserDomain user = new UserDomain(userDto);
        assertNotNull(user);
        assertEquals(userDto.getId(), user.getId().value());
        assertEquals(userDto.getName(), user.getName().value());
        assertEquals(userDto.getEmail(), user.getEmail().value());
        assertEquals(userDto.getPassword(), user.getPassword().value());
        assertFalse(user.isBanned());
    }

    @Test
    @DisplayName("Test banUser method when user is not banned")
    void testBanUserNotBanned() {
        Id id = Id.create("123456");
        Name name = Name.create("John Doe");
        Email email = Email.create("john.doe@example.com");
        Password password = Password.create("Abcdef123!@#");
        UserDomain user = new UserDomain(id, name, email, password, Role.Customer, false);
        assertTrue(user.banUser());
        assertTrue(user.isBanned());
    }

    @Test
    @DisplayName("Test banUser method when user is already banned")
    void testBanUserAlreadyBanned() {
        Id id = Id.create("123456");
        Name name = Name.create("John Doe");
        Email email = Email.create("john.doe@example.com");
        Password password = Password.create("Abcdef123!@#");
        UserDomain user = new UserDomain(id, name, email, password, Role.Customer, false);
        user.banUser();
        assertFalse(user.banUser());
        assertTrue(user.isBanned());
    }
}