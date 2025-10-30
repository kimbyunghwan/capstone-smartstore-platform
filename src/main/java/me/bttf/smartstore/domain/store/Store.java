package me.bttf.smartstore.domain.store;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.bttf.smartstore.domain.common.BaseEntity;
import me.bttf.smartstore.domain.member.Member;
import me.bttf.smartstore.domain.product.ProductStatus;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Store extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_member_id", nullable = false)
    private Member owner;

    @Column(name = "store_name", nullable = false, length = 100)
    private String storeName;

    @Column(name = "description", length = 255)
    private String description;

    // ENUM('ACTIVE','INACTIVE','SUSPENDED') NOT NULL
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private StoreStatus status;

    @Builder
    private Store(Member owner, String storeName, String description, StoreStatus status) {
        if (owner == null) throw new IllegalArgumentException("owner 필수");
        if (storeName == null || storeName.isBlank()) throw new IllegalArgumentException("storeName은 필수");
        if (status == null) throw new IllegalArgumentException("status는 필수");
        this.owner = owner;
        this.storeName = storeName;
        this.description = description;
        this.status = status;
    }
}
