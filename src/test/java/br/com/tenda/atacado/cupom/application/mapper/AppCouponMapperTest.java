package br.com.tenda.atacado.cupom.application.mapper;

import br.com.tenda.atacado.cupom.application.input.CouponInput;
import br.com.tenda.atacado.cupom.application.output.CouponOutput;
import br.com.tenda.atacado.cupom.core.domain.Coupon;
import br.com.tenda.atacado.cupom.core.exception.DomainException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AppCouponMapperTest {

    private final AppCouponMapper mapper = Mappers.getMapper(AppCouponMapper.class);

    @Test
    @DisplayName("should map CouponInput to Coupon domain and validate successfully")
    void shouldMapInputToDomain() {
        CouponInput input = new CouponInput(
                "a-bc1/23",
                "Cupom de teste",
                new BigDecimal("10.0"),
                LocalDate.now().plusDays(1),
                true
        );

        Coupon domain = mapper.toDomain(input);

        assertNotNull(domain);
        assertEquals("a-bc1/23", domain.getCode());
        assertEquals("Cupom de teste", domain.getDescription());
        assertEquals(new BigDecimal("10.0"), domain.getDiscountValue());
    }

    @Test
    @DisplayName("should throw DomainException when mapping invalid CouponInput")
    void shouldThrowDomainExceptionWhenInvalidInput() {
        CouponInput input = new CouponInput(
                "123",
                "",
                new BigDecimal("0.1"),
                LocalDate.now().minusDays(1),
                false
        );

        assertThrows(DomainException.class, () -> mapper.toDomain(input));
    }

    @Test
    @DisplayName("should map Coupon domain to CouponOutput correctly")
    void shouldMapDomainToOutput() {
        Coupon domain = Coupon.builder()
                .code("ABC123")
                .description("Cupom saída")
                .discountValue(new BigDecimal("20.0"))
                .expirationDate(LocalDate.of(2099, 12, 31))
                .published(true)
                .build();

        CouponOutput out = mapper.toOutput(domain);

        assertNotNull(out);
        assertEquals("ABC123", out.getCode());
        assertEquals("Cupom saída", out.getDescription());
        assertEquals(new BigDecimal("20.0"), out.getDiscountValue());
        assertEquals(LocalDate.of(2099, 12, 31), out.getExpirationDate());
        assertEquals(true, out.isPublished());
    }
}

