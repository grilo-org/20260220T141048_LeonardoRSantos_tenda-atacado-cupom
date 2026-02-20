package br.com.tenda.atacado.cupom.core.usecase.coupon;

import br.com.tenda.atacado.cupom.core.domain.Coupon;
import br.com.tenda.atacado.cupom.core.exception.DomainException;
import br.com.tenda.atacado.cupom.core.port.CouponPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SaveCouponUseCase {

    private final CouponPort couponPort;

    @Transactional
    public Coupon save(Coupon domain) {
        String code = domain.getCode();
        if (code != null) {
            String normalized = code.replaceAll("[^A-Za-z0-9]", "").toUpperCase();
            domain.setCode(normalized);
        }

        if (couponPort.existsByCode(domain.getCode())) {
            throw new DomainException("Já existe um cupom com o código informado");
        }

        return couponPort.save(domain);
    }
}