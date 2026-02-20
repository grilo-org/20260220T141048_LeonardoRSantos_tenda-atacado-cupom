package br.com.tenda.atacado.cupom.infrastructure.repository.adapter;

import br.com.tenda.atacado.cupom.core.domain.Coupon;
import br.com.tenda.atacado.cupom.infrastructure.mapper.CouponMapper;
import br.com.tenda.atacado.cupom.infrastructure.persistence.CouponEntity;
import br.com.tenda.atacado.cupom.infrastructure.repository.CouponRepository;

import br.com.tenda.atacado.cupom.infrastructure.repository.adapter.fixture.CouponFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponRepositoryAdapterTest {

    @Mock
    private CouponRepository repository;

    @Spy
    private CouponMapper mapper = Mappers.getMapper(CouponMapper.class);

    @InjectMocks
    private CouponRepositoryAdapter adapter;

    @Test
    @DisplayName("should save coupon using mapper")
    void shouldSaveCouponUsingMapper() {
        Coupon coupon = CouponFixture.createCoupon();
        CouponEntity savedEntity = CouponFixture.createCouponEntity();

        when(repository.save(any(CouponEntity.class))).thenReturn(savedEntity);

        Coupon saved = adapter.save(coupon);

        verify(repository).save(any(CouponEntity.class));
        assertEquals("ABC123", saved.getCode());
        assertEquals("Cupom teste", saved.getDescription());
    }

    @Test
    @DisplayName("should find coupon by code using mapper")
    void shouldFindCouponByCodeUsingMapper() {
        CouponEntity entity = CouponFixture.createCouponEntity();

        when(repository.findByCode("ABC123")).thenReturn(Optional.of(entity));

        Optional<Coupon> result = adapter.findByCode("ABC123");

        assertTrue(result.isPresent());
        assertEquals("ABC123", result.get().getCode());
        verify(repository).findByCode("ABC123");
    }
}

