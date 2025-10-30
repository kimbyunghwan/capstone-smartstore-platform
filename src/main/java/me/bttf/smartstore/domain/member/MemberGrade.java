package me.bttf.smartstore.domain.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(
        name = "member_grade",
        indexes = {
                @Index(name = "ix_member_grade_name", columnList = "grade_name")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "grade_id"))
public class MemberGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "grade_id")
    private Long id; // grade_id

    @Column(name = "grade_name", nullable = false, unique = true, length = 50)
    private String gradeName; // 등급명 (일반, 우수, VIP 등)

    @Column(name = "benefit_note", length = 255)
    private String benefitNote; // 혜택 설명

    @Builder
    private MemberGrade(String gradeName, String benefitNote) {
        if (gradeName == null || gradeName.isBlank()) {
            throw new IllegalArgumentException("gradeName은 필수입니다.");
        }
        this.gradeName = gradeName;
        this.benefitNote = benefitNote;
    }
}
