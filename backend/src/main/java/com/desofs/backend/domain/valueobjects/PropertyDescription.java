package com.desofs.backend.domain.valueobjects;

import static org.apache.commons.lang3.Validate.notNull;
import static org.springframework.util.Assert.isTrue;

public class PropertyDescription {

    private final String description;

    public PropertyDescription(String description) {
        notNull(description, "Property description must not be null.");
        isTrue(!description.trim().isEmpty(), "Property description must not be empty.");
        this.description = description;
    }

    public String getDescription() {
        return new String(description);
    }
}