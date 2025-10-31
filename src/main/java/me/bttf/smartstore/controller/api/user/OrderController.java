package me.bttf.smartstore.controller.api.user;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.dto.order.CreateOrderReq;
import me.bttf.smartstore.dto.order.OrderRes;
import me.bttf.smartstore.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class OrderController {

    private final OrderService orderService;

    // 7. 주문 생성 (/order)
    @PostMapping("/order")
    @Transactional
    public OrderRes create(@RequestBody @Valid CreateOrderReq req) {
        return orderService.create(req);
    }

    // 8. 주문 내역 (/orders?memberId=1&page=0&size=10&sort=orderId,desc)
    @GetMapping("/orders")
    public Page<OrderRes> myOrders(@RequestParam Long memberId,
                                   @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC)
                                   Pageable pageable) {
        return orderService.myOrders(memberId, pageable);
    }
}
