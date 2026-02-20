package br.com.tenda.atacado.cupom.infrastructure.persistence;

import br.com.tenda.atacado.cupom.core.domain.enums.CouponStatusType;
import br.com.tenda.atacado.cupom.infrastructure.repository.converter.CouponStatusTypeConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "coupon")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CouponEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "external_id", updatable = false, nullable = false, unique = true)
    private UUID externalId;

    @Column(name = "code", nullable = false, unique = true, length = 6)
    private String code;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "coupon_status", nullable = false)
    @Convert(converter = CouponStatusTypeConverter.class)
    private CouponStatusType couponStatusType;

    @Column(name = "discount_value", nullable = false)
    private BigDecimal discountValue;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "published", nullable = false)
    private boolean published;

    @Column(name = "redeemed", nullable = false)
    private boolean redeemed;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void generateExternalId() {
        if (externalId == null) {
            externalId = UUID.randomUUID();
        }
    }
}
