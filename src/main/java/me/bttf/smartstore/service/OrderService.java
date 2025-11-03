package me.bttf.smartstore.service;


import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.domain.common.Money;
import me.bttf.smartstore.domain.member.Member;
import me.bttf.smartstore.domain.order.AddressSnapshot;
import me.bttf.smartstore.domain.order.Order;
import me.bttf.smartstore.domain.order.OrderItem;
import me.bttf.smartstore.domain.order.OrderStatus;
import me.bttf.smartstore.domain.product.Product;
import me.bttf.smartstore.domain.product.ProductOption;
import me.bttf.smartstore.dto.order.CreateOrderReq;
import me.bttf.smartstore.dto.order.OrderRes;
import me.bttf.smartstore.exception.ResourceNotFoundException;
import me.bttf.smartstore.repository.MemberRepository;
import me.bttf.smartstore.repository.OrderRepository;
import me.bttf.smartstore.repository.ProductOptionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepo;
    private final MemberRepository memberRepo;
    private final ProductOptionRepository optionRepo;
    private final InventoryService inventoryService; // 이미 구현됨 (in/out/adjust)

    public OrderRes create(CreateOrderReq req) {
        Member member = memberRepo.findById(req.memberId())
                .orElseThrow(() -> new ResourceNotFoundException("회원이 없습니다. id=" + req.memberId()));

        // 옵션들 일괄 로딩 & 존재/재고 검증
        Map<Long, ProductOption> optionMap = loadOptions(req.items());

        // 주문 엔티티 생성 + 주소 스냅샷
        AddressSnapshot snap = new AddressSnapshot(
                req.recvName(), req.recvPhone(), req.recvZipcode(), req.recvAddr1(), req.recvAddr2());

        Order order = new Order(member, snap, req.recvMemo()); // (도메인 메서드 추가 버전, 아래 4) 참조)
        BigDecimal total = BigDecimal.ZERO;

        // 아이템 생성 & 재고 차감
        for (CreateOrderReq.Item it : req.items()) {
            ProductOption opt = optionMap.get(it.optionId());
            Product prod = opt.getProduct();

            // 재고 검사
            if (opt.getStockQty() < it.qty()) {
                throw new IllegalStateException("재고 부족: optionId=" + opt.getId());
            }

            // 스냅샷
            String productName = prod.getName();
            String optionName  = opt.getOptionName();
            BigDecimal unitPrice = opt.getPrice().getAmount();

            OrderItem oi = new OrderItem(order, prod, opt, productName, optionName, Money.of(unitPrice), it.qty());
            order.addItem(oi);

            total = total.add(unitPrice.multiply(BigDecimal.valueOf(it.qty())));

            // 재고 출고(예약/차감) – 비즈니스에 맞게 reason/orderId 전달
            inventoryService.out(opt.getId(), it.qty(), "고객주문", null);
        }

        order.setTotal(Money.of(total));
        order.changeStatus(OrderStatus.PENDING); // 결제 전/주문 생성 상태

        Order saved = orderRepo.save(order);
        return toRes(saved);
    }

    @Transactional(readOnly = true)
    public Page<OrderRes> myOrders(Long memberId, Pageable pageable) {
        return orderRepo.findByMemberIdWithItems(memberId, pageable).map(this::toRes);
    }

    // -- helpers --
    private Map<Long, ProductOption> loadOptions(List<CreateOrderReq.Item> items) {
        List<Long> ids = items.stream().map(CreateOrderReq.Item::optionId).toList();
        List<ProductOption> opts = optionRepo.findAllById(ids);
        if (opts.size() != ids.size()) {
            Set<Long> found = opts.stream().map(ProductOption::getId).collect(java.util.stream.Collectors.toSet());
            ids.stream().filter(id -> !found.contains(id)).findFirst()
                    .ifPresent(missing -> { throw new ResourceNotFoundException("옵션 없음: id=" + missing); });
        }
        Map<Long, ProductOption> map = new HashMap<>();
        for (ProductOption o : opts) map.put(o.getId(), o);
        return map;
    }

    private OrderRes toRes(Order o) {
        return new OrderRes(
                o.getId(),
                o.getMember().getId(),
                o.getStatus().name(),
                o.getRecvAddress().getName(),
                o.getRecvAddress().getPhone(),
                o.getRecvAddress().getZipcode(),
                o.getRecvAddress().getAddr1(),
                o.getRecvAddress().getAddr2(),
                o.getRecvMemo(),
                o.getOrderTotal().getAmount(),
                o.getCreatedAt(),
                o.getItems().stream()
                        .map(oi -> new OrderRes.OrderItemRes(
                                oi.getId(),
                                oi.getProduct().getId(),
                                oi.getOption().getId(),
                                oi.getProductName(),
                                oi.getOptionName(),
                                oi.getUnitPrice().getAmount(),
                                oi.getQty()
                        ))
                        .toList()
        );
    }
}
