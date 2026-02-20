package br.com.tenda.atacado.cupom.infrastructure.mapper;

import br.com.tenda.atacado.cupom.core.domain.Coupon;
import br.com.tenda.atacado.cupom.infrastructure.persistence.CouponEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CouponMapper {

    @Mapping(target = "externalId", source = "id")
    @Mapping(target = "couponStatusType", source = "status")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    CouponEntity toEntity(Coupon coupon);

    @Mapping(target = "id", source = "externalId")
    @Mapping(target = "status", source = "couponStatusType")
    Coupon toDomain(CouponEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "externalId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "couponStatusType", source = "status")
    void updateEntity(@MappingTarget CouponEntity entity, Coupon coupon);
}
