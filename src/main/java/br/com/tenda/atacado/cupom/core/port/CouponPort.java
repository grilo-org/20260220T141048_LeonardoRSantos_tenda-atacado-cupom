package br.com.tenda.atacado.cupom.core.port;

import br.com.tenda.atacado.cupom.core.domain.Coupon;

import java.util.Optional;
import java.util.UUID;

public interface CouponPort {

    Coupon save(Coupon coupon);

    boolean existsByCode(String code);

    Optional<Coupon> findByCode(String code);

    Optional<Coupon> findById(UUID id);
}

