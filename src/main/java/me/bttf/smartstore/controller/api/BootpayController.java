package me.bttf.smartstore.controller.api;

import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.domain.product.ProductOption;
import me.bttf.smartstore.dto.BootpayVerifyReq;
import me.bttf.smartstore.exception.ResourceNotFoundException;
import me.bttf.smartstore.repository.ProductOptionRepository;
import me.bttf.smartstore.service.BootpayService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/pay/bootpay")
@RequiredArgsConstructor
public class BootpayController {
    private final BootpayService bootpayService;
    private final ProductOptionRepository optionRepo;

    @PostMapping("/verify")
    public ResponseEntity<Map<String, Object>> verify(@RequestBody BootpayVerifyReq req) {
        if (req.optionId() == null)
            return ResponseEntity.badRequest().body(Map.of("status","fail","message","optionId 누락"));
        if (req.qty() == null || req.qty() <= 0)
            return ResponseEntity.badRequest().body(Map.of("status","fail","message","수량 오류"));

        ProductOption opt = optionRepo.findById(req.optionId())
                .orElseThrow(() -> new ResourceNotFoundException("옵션 없음: " + req.optionId()));

        int unitPrice = opt.getPrice().getAmount().intValue();
        int expectedAmount = unitPrice * req.qty();

        boolean ok = bootpayService.verifyPayment(req.receiptId(), expectedAmount);
        if (!ok) {
            return ResponseEntity.badRequest().body(Map.of("status","fail","message","영수증 검증 실패"));
        }

        return ResponseEntity.ok(Map.of("status","success","orderCode", req.orderId()));
    }
}
