<<<<<<< HEAD
package me.bttf.smartstore.domain.common;

public class Money {
=======
package me.bttf.smartstore.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor
public class Money {
    @Column(name = "amount", nullable = false)
    private Long amount;

    public Money(long amount){
        this.amount = amount;
    }
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
