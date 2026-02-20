package br.com.tenda.atacado.cupom.core.usecase.coupon;

import br.com.tenda.atacado.cupom.core.domain.Coupon;
import br.com.tenda.atacado.cupom.core.domain.enums.CouponStatusType;
import br.com.tenda.atacado.cupom.core.exception.DomainException;
import br.com.tenda.atacado.cupom.core.port.CouponPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GetCouponByIdUseCaseTest {

    @Mock
    private CouponPort couponPort;

    @InjectMocks
    private GetCouponByIdUseCase useCase;

    @Test
    @DisplayName("should return coupon when id exists")
    void shouldReturnCouponWhenIdExists() {
        UUID id = UUID.fromString("598c5a85-46d6-4c69-8513-6ecfb9b5d7e2");

        Coupon coupon = Coupon.builder()
                .id(id)
                .code("ABC123")
                .description("Cupom teste")
                .discountValue(new BigDecimal("10.0"))
                .expirationDate(LocalDate.of(2099, 12, 31))
                .published(true)
                .redeemed(false)
                .status(CouponStatusType.ACTIVE)
                .build();

        when(couponPort.findById(id)).thenReturn(Optional.of(coupon));

        Coupon result = useCase.execute(id);

        assertEquals(id, result.getId());
        assertEquals("ABC123", result.getCode());
        assertEquals(CouponStatusType.ACTIVE, result.getStatus());
        verify(couponPort).findById(id);
    }

    @Test
    @DisplayName("should throw DomainException when coupon not found")
    void shouldThrowDomainExceptionWhenCouponNotFound() {
        UUID id = UUID.randomUUID();

        when(couponPort.findById(id)).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> useCase.execute(id));
        verify(couponPort).findById(id);
    }
}
