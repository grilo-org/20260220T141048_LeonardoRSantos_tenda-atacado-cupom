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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteCouponUseCaseTest {

    @Mock
    private CouponPort couponPort;

    @InjectMocks
    private DeleteCouponUseCase useCase;

    @Test
    @DisplayName("should soft delete coupon changing status to DELETED")
    void shouldSoftDeleteCoupon() {
        Coupon coupon = Coupon.builder()
                .code("ABC123")
                .description("Cupom teste")
                .discountValue(new BigDecimal("10.0"))
                .expirationDate(LocalDate.now().plusDays(1))
                .published(true)
                .status(CouponStatusType.ACTIVE)
                .build();

        when(couponPort.findByCode("ABC123")).thenReturn(Optional.of(coupon));

        useCase.execute("ABC123");

        assertEquals(CouponStatusType.DELETED, coupon.getStatus());
        assertNotNull(coupon.getDeletedAt());
        verify(couponPort).findByCode("ABC123");
        verify(couponPort).save(coupon);
    }

    @Test
    @DisplayName("should throw DomainException when coupon not found")
    void shouldThrowDomainExceptionWhenCouponNotFound() {
        when(couponPort.findByCode("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(DomainException.class, () -> useCase.execute("UNKNOWN"));
        verify(couponPort).findByCode("UNKNOWN");
        verify(couponPort, never()).save(any(Coupon.class));
    }

    @Test
    @DisplayName("should throw DomainException when coupon already deleted")
    void shouldThrowDomainExceptionWhenCouponAlreadyDeleted() {
        Coupon coupon = Coupon.builder()
                .code("ABC123")
                .description("Cupom teste")
                .discountValue(new BigDecimal("10.0"))
                .expirationDate(LocalDate.now().plusDays(1))
                .published(true)
                .status(CouponStatusType.DELETED)
                .build();

        when(couponPort.findByCode("ABC123")).thenReturn(Optional.of(coupon));

        assertThrows(DomainException.class, () -> useCase.execute("ABC123"));
        verify(couponPort).findByCode("ABC123");
        verify(couponPort, never()).save(any(Coupon.class));
    }
}
