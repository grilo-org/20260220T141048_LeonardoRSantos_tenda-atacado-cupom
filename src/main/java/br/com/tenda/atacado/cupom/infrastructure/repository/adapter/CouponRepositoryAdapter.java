package br.com.tenda.atacado.cupom.infrastructure.repository.adapter;

import br.com.tenda.atacado.cupom.core.domain.Coupon;
import br.com.tenda.atacado.cupom.core.port.CouponPort;
import br.com.tenda.atacado.cupom.infrastructure.mapper.CouponMapper;
import br.com.tenda.atacado.cupom.infrastructure.persistence.CouponEntity;
import br.com.tenda.atacado.cupom.infrastructure.repository.CouponRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CouponRepositoryAdapter implements CouponPort {

    private final CouponRepository repository;
    private final CouponMapper mapper;

    @Override
    public Coupon save(Coupon coupon) {
        CouponEntity entity;
        if (coupon.getId() != null) {
            entity = repository.findByExternalId(coupon.getId())
                    .map(existing -> {
                        mapper.updateEntity(existing, coupon);
                        return existing;
                    })
                    .orElseGet(() -> mapper.toEntity(coupon));
        } else {
            entity = mapper.toEntity(coupon);
        }
        CouponEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public boolean existsByCode(String code) {
        return repository.existsByCode(code);
    }

    @Override
    public Optional<Coupon> findByCode(String code) {
        return repository.findByCode(code).map(mapper::toDomain);
    }

    @Override
    public Optional<Coupon> findById(UUID id) {
        return repository.findByExternalId(id).map(mapper::toDomain);
    }
}