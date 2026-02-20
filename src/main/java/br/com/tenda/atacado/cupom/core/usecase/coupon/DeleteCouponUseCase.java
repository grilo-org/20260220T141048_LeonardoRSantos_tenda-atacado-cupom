package br.com.tenda.atacado.cupom.core.usecase.coupon;

import br.com.tenda.atacado.cupom.core.domain.Coupon;
import br.com.tenda.atacado.cupom.core.exception.DomainException;
import br.com.tenda.atacado.cupom.core.port.CouponPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeleteCouponUseCase {

    private final CouponPort couponPort;

    @Transactional
    public void execute(String code) {
        Coupon coupon = couponPort.findByCode(code)
                .orElseThrow(() -> new DomainException("Cupom não encontrado"));

        coupon.delete();

        couponPort.save(coupon);
    }
}

