package br.com.tenda.atacado.cupom.core.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum CouponStatusType {
    ACTIVE(1, "Ativo"),
    INACTIVE(2, "Inativo"),
    DELETED(3, "Deletado");

    private final Integer code;
    private final String description;

    CouponStatusType(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    @JsonCreator
    public static CouponStatusType fromCode(Integer code) {
        return Arrays.stream(values())
                .filter(e -> e.getCode().equals(code))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("O tipo de status é inválido: " + code));
    }
}
