package br.com.tenda.atacado.cupom.application.output;

import br.com.tenda.atacado.cupom.core.domain.enums.CouponStatusType;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CouponOutput {

    private UUID id;
    private String code;
    private String description;
    private BigDecimal discountValue;
    @JsonSerialize(using = LocalDateIsoSerializer.class)
    private LocalDate expirationDate;
    private CouponStatusType status;
    private boolean published;
    private boolean redeemed;
}
