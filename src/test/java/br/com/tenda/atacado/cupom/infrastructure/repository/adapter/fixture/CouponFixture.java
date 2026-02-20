package br.com.tenda.atacado.cupom.infrastructure.repository.adapter.fixture;

import br.com.tenda.atacado.cupom.core.domain.Coupon;
import br.com.tenda.atacado.cupom.core.domain.enums.CouponStatusType;
import br.com.tenda.atacado.cupom.infrastructure.persistence.CouponEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class CouponFixture {

    public static Coupon createCoupon() {
        return Coupon.builder()
                .id(UUID.fromString("598c5a85-46d6-4c69-8513-6ecfb9b5d7e2"))
                .code("ABC123")
                .description("Cupom teste")
                .discountValue(new BigDecimal("10.0"))
                .expirationDate(LocalDate.of(2099, 12, 31))
                .published(true)
                .redeemed(false)
                .status(CouponStatusType.ACTIVE)
                .build();
    }

    public static CouponEntity createCouponEntity() {
        CouponEntity entity = new CouponEntity();
        entity.setId(1L);
        entity.setExternalId(UUID.fromString("598c5a85-46d6-4c69-8513-6ecfb9b5d7e2"));
        entity.setCode("ABC123");
        entity.setDescription("Cupom teste");
        entity.setDiscountValue(new BigDecimal("10.0"));
        entity.setExpirationDate(LocalDate.of(2099, 12, 31));
        entity.setPublished(true);
        entity.setRedeemed(false);
        entity.setCouponStatusType(CouponStatusType.ACTIVE);
        return entity;
    }
}
