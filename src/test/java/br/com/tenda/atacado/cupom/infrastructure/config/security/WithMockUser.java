package br.com.tenda.atacado.cupom.infrastructure.config.security;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = CustomSecurityContextFactory.class)
public @interface WithMockUser {
    String username() default "testuser";
    String identifier() default "0c8d3c21-dcf8-4271-9b45-93b0bd212ab8";
    String[] roles() default {"USER"};
}
