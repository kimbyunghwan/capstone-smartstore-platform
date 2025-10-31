package me.bttf.smartstore.domain.member;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.bttf.smartstore.domain.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "member",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_member_email",    columnNames = "email")
        },
        indexes = {
                @Index(name = "ix_member_grade", columnList = "grade_id")
        }
)
@AttributeOverride(name = "id", column = @Column(name = "member_id"))
public class Member extends BaseEntity {

    @Column(name = "email", length = 120, nullable = false)
    private String email; // 로그인은 이메일로

    @Column(name = "name", length = 60, nullable = false)
    private String name;

    @Column(name = "phone", length = 30)
    private String phone;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private MemberGrade grade;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    @Builder
    private Member(String email, String name, String phone,
                   String password, MemberGrade grade) {
        if (email == null || email.isBlank())     throw new IllegalArgumentException("email 필수");
        if (name == null  || name.isBlank())      throw new IllegalArgumentException("name 필수");
        if (password == null || password.isBlank()) throw new IllegalArgumentException("password 필수");
        this.email = email;
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.grade = grade;
    }
}
