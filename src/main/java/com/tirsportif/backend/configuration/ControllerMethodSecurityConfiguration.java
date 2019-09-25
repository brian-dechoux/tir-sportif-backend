package com.tirsportif.backend.configuration;

import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ControllerMethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        return new CustomMethodSecurityExpressionHandler();
    }

}
