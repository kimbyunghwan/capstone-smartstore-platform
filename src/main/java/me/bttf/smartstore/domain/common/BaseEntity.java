<<<<<<< HEAD
package me.bttf.smartstore.domain.common;

public class BaseEntity {
=======
package me.bttf.smartstore.common;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
public class BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(updatable = false)
    private LocalDateTime createdAt =  LocalDateTime.now();
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
