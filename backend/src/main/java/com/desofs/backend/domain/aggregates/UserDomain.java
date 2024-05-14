package com.desofs.backend.domain.aggregates;

import com.desofs.backend.domain.valueobjects.Email;
import com.desofs.backend.domain.valueobjects.Id;
import com.desofs.backend.domain.valueobjects.Name;
import com.desofs.backend.domain.valueobjects.Password;
import com.desofs.backend.dtos.UserDto;
import lombok.Getter;

import static org.apache.commons.lang3.Validate.notNull;

public class UserDomain {

    private Id id;
    private Name name;
    private Email email;
    private Password password;
    @Getter
    private boolean banned;

    public UserDomain(Id id, Name name, Email email, Password password, boolean banned) {
        notNull(id,
                "Id must not be null.");
        notNull(name,
                "Name must not be null.");
        notNull(email,
                "Email must not be null.");
        notNull(password,
                "Password must not be null.");

        this.id = id.copy();
        this.name = name.copy();
        this.email = email.copy();
        this.password = password.copy();
        this.banned = false;
    }

    public UserDomain(UserDto userDto) {
        this(Id.create(userDto.getId()),
                Name.create(userDto.getName()),
                Email.create(userDto.getEmail()),
                Password.create(userDto.getPassword()),
                userDto.isBanned());
    }

    public boolean banUser() {
        if (this.banned) {
            return false;
        } else {
            this.banned = true;
            return true;
        }
    }

    public Id getId() {
        return this.id.copy();
    }

    public Name getName() {
        return this.name.copy();
    }

    public Email getEmail() {
        return this.email.copy();
    }

    public Password getPassword() {
        return this.password.copy();
    }


}
