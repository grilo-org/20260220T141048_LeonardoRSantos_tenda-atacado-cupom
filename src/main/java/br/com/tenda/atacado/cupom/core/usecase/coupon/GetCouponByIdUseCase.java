package br.com.tenda.atacado.cupom.core.usecase.coupon;

import br.com.tenda.atacado.cupom.core.domain.Coupon;
import br.com.tenda.atacado.cupom.core.exception.DomainException;
import br.com.tenda.atacado.cupom.core.port.CouponPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetCouponByIdUseCase {

    private final CouponPort couponPort;

    @Transactional(readOnly = true)
    public Coupon execute(UUID id) {
        return couponPort.findById(id)
                .orElseThrow(() -> new DomainException("Cupom não encontrado"));
    }
}
