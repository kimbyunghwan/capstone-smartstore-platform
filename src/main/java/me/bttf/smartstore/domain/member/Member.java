<<<<<<< HEAD
package me.bttf.smartstore.domain.member;

public class Member {
=======
package me.bttf.smartstore.member;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Member {
    private String loginId;
    private String email;
    private String name;
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grade_id")
    private MemberGrade grade;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
