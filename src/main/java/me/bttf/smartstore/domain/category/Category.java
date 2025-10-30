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
@AttributeOverride(name = "id", column = @Column(name = "category_id"))
public class Category extends BaseEntity {

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private Integer depth;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> children = new ArrayList<>();

    public Category(String name, int depth, Category parent) {
        if (depth < 1 || depth > 3) {
            throw new IllegalArgumentException("카테고리 깊이는 1~3만 허용");
        }
        this.name = name;
        this.depth = depth;
        this.parent = parent;
    }
}
