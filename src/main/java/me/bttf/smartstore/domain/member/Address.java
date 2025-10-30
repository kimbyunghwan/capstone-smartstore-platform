<<<<<<< HEAD
package me.bttf.smartstore.domain.member;

public class Address {
=======
package me.bttf.smartstore.member;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.Getter;

@Entity
@Getter
public class Address {
    @ManyToOne(fetch = FetchType.LAZY) private Member member;
    private String receiverName;
    private String phone;
    private String zipcode; private String addr1; private String addr2;
    private boolean isDefault;
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
