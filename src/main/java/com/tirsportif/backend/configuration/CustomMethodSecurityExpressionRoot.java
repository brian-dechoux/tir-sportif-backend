package com.tirsportif.backend.configuration;

import com.tirsportif.backend.model.AuthorityType;
import com.tirsportif.backend.model.User;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

@Getter
@Setter
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private Object target;

    public CustomMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    public boolean authorizedFor(String expected) {
        AuthorityType expectedAuthorityType = AuthorityType.valueOf(expected);
        Object authentication = getAuthentication();
        if (authentication instanceof User) {
            User connected = (User) authentication;
            return connected.getAuthority().getLabel().ordinal() <= expectedAuthorityType.ordinal();
        }
        return false;
    }

    @Override
    public Object getThis() {
        return target;
    }

}
