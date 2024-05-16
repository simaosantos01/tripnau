package com.desofs.backend.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public class Role implements GrantedAuthority {

    public static final String BusinessAdmin = "BUSINESSADMIN";

    public static final String PropertyOwner = "PROPERTYOWNER";

    public static final String Customer = "CUSTOMER";

    String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}