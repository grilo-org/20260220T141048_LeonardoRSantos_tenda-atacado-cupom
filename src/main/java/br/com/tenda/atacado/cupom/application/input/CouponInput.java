package br.com.tenda.atacado.cupom.application.input;


import br.com.tenda.atacado.cupom.application.util.LocalDateDeserializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CouponInput {

    @NotBlank(message = "Código do cupom é obrigatório.")
    private String code;

    @NotBlank(message = "Descrição é obrigatória.")
    @Size(max = 500, message = "Descrição deve ter no máximo 500 caracteres.")
    private String description;

    @DecimalMin(value = "0.5", message = "Valor de desconto mínimo é 0,5.")
    private BigDecimal discountValue;

    @FutureOrPresent(message = "Data de expiração não pode estar no passado.")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDate expirationDate;

    private boolean published;
}
