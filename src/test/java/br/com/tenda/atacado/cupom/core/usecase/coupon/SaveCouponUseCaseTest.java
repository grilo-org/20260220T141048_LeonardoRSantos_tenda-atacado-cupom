package br.com.tenda.atacado.cupom.core.usecase.coupon;

import br.com.tenda.atacado.cupom.core.domain.Coupon;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SaveCouponUseCaseTest {

    @Mock
    private CouponPort couponPort;

    @InjectMocks
    private SaveCouponUseCase useCase;

    @Test
    @DisplayName("should normalize code and save coupon when not exists")
    void shouldNormalizeCodeAndSaveCoupon() {
        Coupon coupon = Coupon.builder()
                .code("a-bc1/23")
                .description("Cupom teste")
                .discountValue(new BigDecimal("10.0"))
                .expirationDate(LocalDate.now().plusDays(1))
                .published(true)
                .build();

        when(couponPort.existsByCode("ABC123")).thenReturn(false);
        when(couponPort.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Coupon saved = useCase.save(coupon);

        assertEquals("ABC123", saved.getCode());
        verify(couponPort).existsByCode("ABC123");
        verify(couponPort).save(any(Coupon.class));
    }

    @Test
    @DisplayName("should save coupon without normalization when code is null")
    void shouldSaveCouponWhenCodeIsNull() {
        Coupon coupon = Coupon.builder()
                .code(null)
                .description("Cupom sem código")
                .discountValue(new BigDecimal("10.0"))
                .expirationDate(LocalDate.now().plusDays(1))
                .published(false)
                .build();

        when(couponPort.existsByCode(null)).thenReturn(false);
        when(couponPort.save(any(Coupon.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Coupon saved = useCase.save(coupon);

        assertEquals(null, saved.getCode());
        verify(couponPort).existsByCode(null);
        verify(couponPort).save(any(Coupon.class));
    }

    @Test
    @DisplayName("should throw DomainException when coupon code already exists")
    void shouldThrowDomainExceptionWhenCouponCodeAlreadyExists() {
        Coupon coupon = Coupon.builder()
                .code("ABC123")
                .description("Cupom duplicado")
                .discountValue(new BigDecimal("10.0"))
                .expirationDate(LocalDate.now().plusDays(1))
                .published(true)
                .build();

        when(couponPort.existsByCode("ABC123")).thenReturn(true);

        assertThrows(DomainException.class, () -> useCase.save(coupon));
        verify(couponPort).existsByCode("ABC123");
        verify(couponPort, never()).save(any(Coupon.class));
    }
}

