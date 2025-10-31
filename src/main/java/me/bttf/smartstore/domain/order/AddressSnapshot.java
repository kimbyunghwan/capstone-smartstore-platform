package me.bttf.smartstore.domain.order;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class AddressSnapshot {
    private String name;
    private String phone;
    private String zipcode;
    private String addr1;
    private String addr2;
}
