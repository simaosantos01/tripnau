package com.desofs.backend.services;

import com.desofs.backend.domain.valueobjects.PhoneNumber;
import com.desofs.backend.dtos.UpdatePasswordDto;
import com.desofs.backend.exceptions.DatabaseException;
import com.desofs.backend.database.repositories.UserRepository;
import com.desofs.backend.domain.aggregates.UserDomain;
import com.desofs.backend.domain.valueobjects.Email;
import com.desofs.backend.domain.valueobjects.Name;
import com.desofs.backend.domain.valueobjects.Password;
import com.desofs.backend.dtos.CreateUserDto;
import com.desofs.backend.dtos.FetchUserDto;
import com.desofs.backend.exceptions.UpdatePasswordException;
import com.mailersend.sdk.MailerSend;
import com.mailersend.sdk.exceptions.MailerSendException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final LoggerService logger;

    @Value("${mailSenderApiKey}")
    private String mailSenderApiKey;

    @Transactional
    public FetchUserDto create(CreateUserDto createUserDTO) throws DatabaseException {
        UserDomain user = new UserDomain(Name.create(createUserDTO.getName()), Email.create(createUserDTO.getEmail()),
                Password.create(createUserDTO.getPassword()), PhoneNumber.create(createUserDTO.getPhoneNumber()),
                createUserDTO.getRole());

        UserDomain userToCreate = new UserDomain(user.getId(), user.getName(), user.getEmail(),
                Password.create(encoder.encode(user.getPassword().value())), user.getPhoneNumber(), user.getRole(),
                user.isBanned());

        this.userRepository.create(userToCreate);
        logger.info("User " + createUserDTO.getEmail() + " registered");
        return new FetchUserDto(user.getId().value(), user.getName().value(), user.getEmail().value(),
                user.getPhoneNumber().value(), user.getRole(), user.isBanned());
    }

    @Transactional
    public FetchUserDto findByEmail(String email) {
        UserDomain user = this.userRepository.findByEmail(email);
        if (user != null) {
            return new FetchUserDto(user.getId().value(), user.getName().value(), user.getEmail().value(),
                    user.getPhoneNumber().value(), user.getRole(), user.isBanned());
        } else {
            return null;
        }
    }

    @Transactional
    public FetchUserDto findById(String id) {
        UserDomain user = this.userRepository.findById(id);
        if (user != null) {
            return new FetchUserDto(user.getId().value(), user.getName().value(), user.getEmail().value(),
                    user.getPhoneNumber().value(), user.getRole(), user.isBanned());
        } else {
            return null;
        }
    }

    @Transactional
    public void updatePassword(UpdatePasswordDto updatePasswordDto, String userId) throws MailerSendException,
            UpdatePasswordException {

        UserDomain user = this.userRepository.findById(userId);
        String currentPassword = encoder.encode(updatePasswordDto.oldPassword());

        if (!currentPassword.equals(user.getPassword().value())) {
            throw new UpdatePasswordException("current password is wrong");
        }

        UserDomain updatedUser = new UserDomain(user.getId(), user.getName(), user.getEmail(),
                Password.create(encoder.encode(updatePasswordDto.newPassword())), user.getPhoneNumber(), user.getRole(),
                user.isBanned());

        com.mailersend.sdk.emails.Email email = new com.mailersend.sdk.emails.Email();
        email.subject = "Hello from MailerSend!";
        email.text = "This is a test email from the MailerSend Java SDK";
        email.addRecipient("Recipient name", "simaosantos01@hotmail.com");
        email.setFrom("Sender name", "sender@example.com");

        MailerSend ms = new MailerSend();
        ms.setToken(mailSenderApiKey);
        ms.emails().send(email);
    }
}

