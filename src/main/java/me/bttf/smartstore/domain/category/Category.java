package me.bttf.smartstore.domain.category;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.bttf.smartstore.domain.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(
        name = "category",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_parent_name", columnNames = {"parent_id", "name"})
        },
        indexes = {
                @Index(name = "ix_category_parent", columnList = "parent_id")
        }
)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int depth;   // 1부터 시작

    public Category(String name, Integer depth, Category parent) {
        this.parent = parent;
        this.name = name;
        // depth가 null이면 부모 기준으로 자동 계산
        this.depth = (depth != null) ? depth : (parent == null ? 1 : parent.getDepth() + 1);
        if (this.depth < 1 || this.depth > 3) {
            throw new IllegalArgumentException("카테고리 깊이는 1~3만 허용");
        }
    }

    /** 부모를 바꾸면 depth도 자동 보정 */
    public void attachTo(Category newParent) {
        this.parent = newParent;
        this.depth = (newParent == null ? 1 : newParent.getDepth() + 1);
    }
}
