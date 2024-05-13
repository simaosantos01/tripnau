package com.desofs.backend.domain.valueobjects;

import static org.apache.commons.lang3.Validate.isTrue;
import static org.apache.commons.lang3.Validate.matchesPattern;

public class PropertyName {

    private final String name;

    public PropertyName(String name) {
        isTrue(name != null && !name.trim().isEmpty(), "Property name must not be null or empty.");
        matchesPattern(name, "^[a-zA-Z0-9]+$", "Property name must contain only letters and digits.");
        this.name = name;
    }

    public String getName() {
        return new String(name);
    }
}
