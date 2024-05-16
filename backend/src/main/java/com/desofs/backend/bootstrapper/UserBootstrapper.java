package com.desofs.backend.bootstrapper;

import com.desofs.backend.database.repositories.UserRepository;
import com.desofs.backend.domain.aggregates.UserDomain;
import com.desofs.backend.domain.enums.Role;
import com.desofs.backend.domain.valueobjects.Email;
import com.desofs.backend.domain.valueobjects.Id;
import com.desofs.backend.domain.valueobjects.Name;
import com.desofs.backend.domain.valueobjects.Password;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserBootstrapper {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    public void run() throws Exception {
        UserDomain admin = new UserDomain(Id.create("a"), Name.create("Admin"),
                Email.create("admin@mail.com"), Password.create(encoder.encode("password")), Role.BusinessAdmin, false);
        if (this.userRepository.findByEmail("admin@mail.com") == null) {
            this.userRepository.create(admin);
        }
    }
}
