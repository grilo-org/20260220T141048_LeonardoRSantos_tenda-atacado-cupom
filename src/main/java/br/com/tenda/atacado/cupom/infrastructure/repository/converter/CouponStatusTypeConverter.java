package br.com.tenda.atacado.cupom.infrastructure.repository.converter;

import br.com.tenda.atacado.cupom.core.domain.enums.CouponStatusType;

import javax.persistence.Converter;


@Converter
public class CouponStatusTypeConverter extends GenericConverter<CouponStatusType, Integer> {
    public CouponStatusTypeConverter() {
        super("code");
    }
}
