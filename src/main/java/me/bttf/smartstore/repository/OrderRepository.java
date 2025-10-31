package me.bttf.smartstore.repository;

import me.bttf.smartstore.domain.order.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @EntityGraph(attributePaths = {"items", "items.product", "items.option"})
    @Query("select o from Order o where o.member.id = :memberId")
    Page<Order> findByMemberIdWithItems(@Param("memberId") Long memberId, Pageable pageable);
}
