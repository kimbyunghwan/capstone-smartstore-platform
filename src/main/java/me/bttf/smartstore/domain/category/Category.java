<<<<<<< HEAD
package me.bttf.smartstore.domain.category;

public class Category {
=======
package me.bttf.smartstore.category;

import jakarta.persistence.*;
import lombok.Getter;
import me.bttf.smartstore.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Category extends BaseEntity {
    private String name;
    private Integer depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();
>>>>>>> ff87ebc (feat: 엔티티 구현, h2-> mysql변경, mysqlDB에 엔티티 테이블 정상 생성 확인)
}
