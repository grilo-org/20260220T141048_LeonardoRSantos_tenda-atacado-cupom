package br.com.tenda.atacado.cupom.application.security;


import br.com.tenda.atacado.cupom.application.properties.SecurityProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Locale;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RoleGuard {
    private final SecurityProperties props;

    public boolean has(Authentication auth, String ruleKey) {
        if (auth == null || ruleKey == null){
            return false;
        }
        String authority = props.getRules().get(ruleKey);
        if (authority == null) {
            return false;
        }
        String expectedAuthority = authority.trim().toUpperCase(Locale.ROOT);
        return auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(Objects::nonNull)
                .map(a -> a.trim().toUpperCase(Locale.ROOT))
                .anyMatch(a -> a.equals(expectedAuthority));
    }

    public boolean hasAny(Authentication auth, String... ruleKeys) {
        return Arrays.stream(ruleKeys).anyMatch(k -> has(auth, k));
    }

    public boolean hasAll(Authentication auth, String... ruleKeys) {
        return Arrays.stream(ruleKeys).allMatch(k -> has(auth, k));
    }
}
