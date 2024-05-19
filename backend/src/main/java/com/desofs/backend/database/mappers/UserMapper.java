package com.desofs.backend.database.mappers;

import com.desofs.backend.database.models.UserDB;
import com.desofs.backend.domain.aggregates.UserDomain;
import com.desofs.backend.domain.valueobjects.Email;
import com.desofs.backend.domain.valueobjects.Id;
import com.desofs.backend.domain.valueobjects.Name;
import com.desofs.backend.domain.valueobjects.Password;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserDomain toDomainObject(UserDB user) {
        return new UserDomain(
                Id.create(user.getId()),
                Name.create(user.getName()),
                Email.create(user.getEmail()),
                Password.create(user.getPassword()),
                user.getRole(),
                user.isBanned());
    }

    public UserDB toDatabaseObject(UserDomain user) {
        return new UserDB(user.getId().value(),
                user.getName().value(),
                user.getEmail().value(),
                user.getPassword().value(),
                user.getRole(),
                user.isBanned());
    }
}