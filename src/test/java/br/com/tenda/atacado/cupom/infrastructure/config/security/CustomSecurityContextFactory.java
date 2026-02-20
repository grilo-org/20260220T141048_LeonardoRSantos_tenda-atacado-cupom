package br.com.tenda.atacado.cupom.infrastructure.config.security;

import br.com.tenda.atacado.cupom.core.domain.UserDetailView;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CustomSecurityContextFactory implements WithSecurityContextFactory<WithMockUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserDetailView userDetailView = UserDetailView.builder()
                .username(customUser.username())
                .profiles(Arrays.stream(customUser.roles()).collect(Collectors.toList()))
                .build();

        List<SimpleGrantedAuthority> grantedAuthorities = Arrays.stream(customUser.roles())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetailView, null, grantedAuthorities);

        context.setAuthentication(authentication);
        return context;
    }
}