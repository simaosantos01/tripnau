package com.desofs.backend.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;

@AllArgsConstructor
public class Role implements GrantedAuthority {

    public static final String BUSINESSADMIN = "BUSINESSADMIN";

    public static final String PROPERTYOWNER = "PROPERTYOWNER";

    public static final String CUSTOMER = "CUSTOMER";

    String authority;

    @Override
    public String getAuthority() {
        return authority;
    }
}