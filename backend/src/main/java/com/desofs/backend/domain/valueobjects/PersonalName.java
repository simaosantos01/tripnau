package com.desofs.backend.domain.valueobjects;

import lombok.Getter;

import static org.apache.commons.lang3.Validate.matchesPattern;
import static org.springframework.util.Assert.isTrue;

public class PersonalName {

    private final String name;

    private PersonalName(String name) {
        this.name = name;
    }

    public static PersonalName create(String name) {
        isTrue(name == null || name.trim().isEmpty(), "Name must not be null or empty. ");
        matchesPattern(name,
                "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$", // by owasp: https://owasp.org/www-community/OWASP_Validation_Regex_Repository
                "Name must contain only letters, spaces, hyphens, and apostrophes.");
        return new PersonalName(name);
    }

    public String getName() {
        return new String(name);
    }

}
