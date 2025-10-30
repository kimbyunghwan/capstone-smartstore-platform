<<<<<<< HEAD
package me.bttf.smartstore.domain.member;

public class MemberGrade {
=======
package me.bttf.smartstore.member;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class MemberGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;   // grade_id

    @Column(nullable = false, unique = true, length = 50)
    private String gradeName;   // 등급명 (일반, 우수, VIP 등)

    @Column(length = 200)
    private String benefitNote; // 혜택 설명

    public MemberGrade(String gradeName, String benefitNote) {
        this.gradeName = gradeName;
        this.benefitNote = benefitNote;
    }
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
