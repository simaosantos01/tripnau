package com.desofs.backend.services;

import com.desofs.backend.database.DatabaseException;
import com.desofs.backend.database.repositories.UserRepository;
import com.desofs.backend.domain.aggregates.UserDomain;
import com.desofs.backend.domain.valueobjects.Email;
import com.desofs.backend.domain.valueobjects.Name;
import com.desofs.backend.domain.valueobjects.Password;
import com.desofs.backend.dtos.CreateUserDto;
import com.desofs.backend.dtos.FetchUserDto;
import com.desofs.backend.dtos.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("UserService")
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    public FetchUserDto create(CreateUserDto createUserDTO) throws DatabaseException {
        UserDomain user = new UserDomain(Name.create(createUserDTO.getName()), Email.create(createUserDTO.getEmail()),
                Password.create(createUserDTO.getPassword()), createUserDTO.getRole(),
                createUserDTO.isBanned());
        this.userRepository.create(user);
        return new FetchUserDto(user.getId().value(), user.getName().value(), user.getEmail().value(),
                user.getRole(), user.isBanned());
    }

    @Transactional
    public void update(UserDto userDto) throws DatabaseException {
        UserDomain user = this.userRepository.findByEmail(userDto.getEmail());
        user.setName(Name.create(userDto.getName()));
        user.setEmail(Email.create(userDto.getEmail()));
        user.setPassword(Password.create(userDto.getPassword()));
        user.setRole(user.getRole());
        //TODO: BAN ???
        this.userRepository.update(user);
    }

    @Transactional
    public FetchUserDto findByEmail(String email) {
        UserDomain user = this.userRepository.findByEmail(email);
        return new FetchUserDto(user.getId().value(), user.getName().value(), user.getEmail().value(),
                user.getRole(), user.isBanned());
    }
}
