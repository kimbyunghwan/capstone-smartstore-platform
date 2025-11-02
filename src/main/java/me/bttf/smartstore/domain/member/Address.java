package me.bttf.smartstore.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.bttf.smartstore.domain.common.BaseEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "address",
        indexes = {
                @Index(name = "ix_address_member", columnList = "member_id")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "address_id"))
public class Address extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String receiverName;
    private String phone;
    private String zipcode;
    private String addr1;
    private String addr2;

    @Column(name = "is_default", nullable = false)
    private boolean isDefault;

    public void setAsDefault(List<Address> existingAddresses) {
        existingAddresses.forEach(addr -> addr.isDefault = false);
        this.isDefault = true;
    }
}
