package br.com.tenda.atacado.cupom.core.domain.enums;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserStatusType {
    ACTIVE(1),
    INACTIVE(2),
    SUSPENDED(3);

    private final Integer code;

    UserStatusType(Integer code) {
        this.code = code;
    }

    public static UserStatusType findByName(String name) {
        return Arrays.stream(UserStatusType.values())
                .filter(st -> st.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}