package me.bttf.smartstore.repository;

import me.bttf.smartstore.domain.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"items", "items.product", "items.option"})
    @Query("select o from Order o where o.member.id = :memberId")
    Page<Order> findByMemberIdWithItems(@Param("memberId") Long memberId, Pageable pageable);

    @Query("select o from Order o left join fetch o.items where o.id = :id")
    Optional<Order> findByIdWithItems(@Param("id") Long id);

    Optional<Order> findByOrderCode(String orderCode);
}
