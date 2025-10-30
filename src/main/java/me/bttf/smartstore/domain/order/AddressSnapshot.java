package me.bttf.smartstore.domain.order;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@Getter
public class AddressSnapshot {
    private String name;
    private String phone;
    private String zipcode;
    private String addr1;
    private String addr2;
}
