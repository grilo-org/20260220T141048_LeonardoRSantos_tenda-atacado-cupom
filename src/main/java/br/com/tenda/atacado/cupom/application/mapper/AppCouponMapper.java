package br.com.tenda.atacado.cupom.application.mapper;

import br.com.tenda.atacado.cupom.application.input.CouponInput;
import br.com.tenda.atacado.cupom.application.output.CouponOutput;
import br.com.tenda.atacado.cupom.core.domain.Coupon;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AppCouponMapper {

    Coupon toDomain(CouponInput input);

    CouponOutput toOutput(Coupon saved);

    @AfterMapping
    default void afterMapping(@MappingTarget Coupon coupon) {
        coupon.validate();
    }
}
