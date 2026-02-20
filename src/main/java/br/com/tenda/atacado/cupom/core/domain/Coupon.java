package br.com.tenda.atacado.cupom.core.domain;

import br.com.tenda.atacado.cupom.core.domain.enums.CouponStatusType;
import br.com.tenda.atacado.cupom.core.exception.DomainException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    private UUID id;
    private String code;
    private String description;
    private BigDecimal discountValue;
    private LocalDate expirationDate;
    private boolean published;
    private boolean redeemed;
    private CouponStatusType status;
    private LocalDateTime deletedAt;

    public void validate() {
        if (code == null || code.trim().isEmpty()) {
            throw new DomainException("Código do cupom é obrigatório");
        }

        String normalized = code.replaceAll("[^A-Za-z0-9]", "").toUpperCase();

        if (normalized.length() != 6) {
            throw new DomainException("Código do cupom deve conter exatamente 6 caracteres alfanuméricos");
        }

        if (description == null || description.trim().isEmpty()) {
            throw new DomainException("Descrição do cupom é obrigatória");
        }

        if (discountValue == null || discountValue.compareTo(new BigDecimal("0.5")) < 0) {
            throw new DomainException("Valor de desconto mínimo é 0,5");
        }

        if (expirationDate == null) {
            throw new DomainException("Data de expiração é obrigatória");
        }

        if (expirationDate.isBefore(LocalDate.now())) {
            throw new DomainException("Data de expiração não pode estar no passado");
        }

        if (this.status == null) {
            this.status = CouponStatusType.ACTIVE;
        }
    }

    public void delete() {
        if (CouponStatusType.DELETED.equals(this.status)) {
            throw new DomainException("Cupom já foi deletado anteriormente");
        }
        this.status = CouponStatusType.DELETED;
        this.deletedAt = LocalDateTime.now();
    }
}
