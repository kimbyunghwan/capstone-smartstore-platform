<<<<<<< HEAD
package me.bttf.smartstore.domain.order;

public class AddressSnapshot {
=======
package me.bttf.smartstore.order;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Embeddable
@Getter
public class AddressSnapshot {
    private String name;
    private String phone;
    private String zipcode;
    private String addr1;
    private String addr2;
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
