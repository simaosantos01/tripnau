package com.desofs.backend.services;

import com.desofs.backend.exceptions.DatabaseException;
import com.desofs.backend.database.repositories.UserRepository;
import com.desofs.backend.domain.aggregates.UserDomain;
import com.desofs.backend.domain.valueobjects.Email;
import com.desofs.backend.domain.valueobjects.Name;
import com.desofs.backend.domain.valueobjects.Password;
import com.desofs.backend.dtos.CreateUserDto;
import com.desofs.backend.dtos.FetchUserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Transactional
    public FetchUserDto create(CreateUserDto createUserDTO) throws DatabaseException {
        UserDomain user = new UserDomain(Name.create(createUserDTO.getName()), Email.create(createUserDTO.getEmail()),
                Password.create(createUserDTO.getPassword()), createUserDTO.getRole());

        UserDomain userToCreate = new UserDomain(user.getId(), user.getName(), user.getEmail(),
                Password.create(encoder.encode(user.getPassword().value())), user.getRole(), user.isBanned());

        this.userRepository.create(userToCreate);
        return new FetchUserDto(user.getId().value(), user.getName().value(), user.getEmail().value(),
                user.getRole(), user.isBanned());
    }

    @Transactional
    public FetchUserDto findByEmail(String email) {
        UserDomain user = this.userRepository.findByEmail(email);
        if (user != null) {
            return new FetchUserDto(user.getId().value(), user.getName().value(), user.getEmail().value(),
                    user.getRole(), user.isBanned());
        } else {
            return null;
        }
    }

    @Transactional
    public FetchUserDto findById(String id) {
        UserDomain user = this.userRepository.findById(id);
        if (user != null) {
            return new FetchUserDto(user.getId().value(), user.getName().value(), user.getEmail().value(),
                    user.getRole(), user.isBanned());
        } else {
            return null;
        }
    }
}

