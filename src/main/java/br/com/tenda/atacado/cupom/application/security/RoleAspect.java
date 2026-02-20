package br.com.tenda.atacado.cupom.application.security;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleAspect {

    private final RoleGuard guard;

    @Around("@annotation(role)")
    public Object onMethod(ProceedingJoinPoint pjp, Role role) throws Throwable {
        enforce(role.value());
        return pjp.proceed();
    }

    @Around("@within(role)")
    public Object onType(ProceedingJoinPoint pjp, Role role) throws Throwable {
        enforce(role.value());
        return pjp.proceed();
    }

    @Around("@annotation(rolesAny)")
    public Object onMethodAny(ProceedingJoinPoint pjp, RolesAny rolesAny) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean ok = Arrays.stream(rolesAny.value()).anyMatch(k -> guard.has(auth, k));
        if (!ok) {
            throw new AccessDeniedException("Access denied (any): " + Arrays.toString(rolesAny.value()));
        }
        return pjp.proceed();
    }

    @Around("@annotation(rolesAll)")
    public Object onMethodAll(ProceedingJoinPoint pjp, RolesAll rolesAll) throws Throwable {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        boolean ok = Arrays.stream(rolesAll.value()).allMatch(k -> guard.has(auth, k));
        if (!ok) {
            throw new AccessDeniedException("Access denied (all): " + Arrays.toString(rolesAll.value()));
        }
        return pjp.proceed();
    }

    private void enforce(String ruleKey) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!guard.has(auth, ruleKey)) {
            throw new AccessDeniedException("Access denied for rule: " + ruleKey);
        }
    }
}
