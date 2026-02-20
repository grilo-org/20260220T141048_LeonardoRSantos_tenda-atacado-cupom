package br.com.tenda.atacado.cupom.infrastructure.repository;

import br.com.tenda.atacado.cupom.infrastructure.persistence.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CouponRepository extends JpaRepository<CouponEntity, Long> {

    boolean existsByCode(String code);

    Optional<CouponEntity> findByCode(String code);

    Optional<CouponEntity> findByExternalId(UUID externalId);
}

