package me.bttf.smartstore.service;

import lombok.RequiredArgsConstructor;
import me.bttf.smartstore.domain.inventory.InventoryTx;
import me.bttf.smartstore.domain.inventory.TxType;
import me.bttf.smartstore.domain.product.ProductOption;
import me.bttf.smartstore.repository.InventoryTxRepository;
import me.bttf.smartstore.repository.ProductOptionRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final ProductOptionRepository optionRepo;
    private final InventoryTxRepository txRepo;

    // 입고
    public InventoryTx in(Long optionId, int qty, String reason) {
        assertPositive(qty, "입고 수량은 1 이상이어야 합니다.");
        return record(optionId, TxType.IN, qty, reason, null);
    }

    // 출고
    public InventoryTx out(Long optionId, int qty, String reason, Long orderId) {
        assertPositive(qty, "출고 수량은 1 이상이어야 합니다.");
        // 재고 마이너스 허용 X라면 여기서 체크
        ProductOption opt = optionRepo.findById(optionId).orElseThrow();
        int current = currentNet(optionId);
        if (current - qty < 0) {
            throw new IllegalStateException("재고 부족: 현재 " + current + ", 요청 " + qty);
        }
        return recordExisting(opt, TxType.OUT, qty, reason, orderId);
    }

    // 조정
    public InventoryTx adjust(Long optionId, int qty, String reason) {
        int projected = currentNet(optionId) + qty;

        if (projected < 0) {
            throw new IllegalStateException("조정 후 재고가 음수가 됩니다. 현재="
                    + currentNet(optionId) + ", 조정=" + qty);
        }
        return record(optionId, TxType.ADJUST, qty, reason, null);
    }

    @Transactional(readOnly = true)
    public List<InventoryTx> history(Long optionId) {
        return txRepo.findByOption_IdOrderByCreatedAtDesc(optionId);
    }

    private InventoryTx record(Long optionId, TxType type, int qty, String reason, Long refId) {
        ProductOption opt = optionRepo.findById(optionId).orElseThrow();
        return recordExisting(opt, type, qty, reason, refId);
    }

    private InventoryTx recordExisting(ProductOption opt, TxType type, int qty, String reason, Long refId) {
        InventoryTx tx = InventoryTx.builder()
                .option(opt)
                .txType(type)
                .qty(qty)
                .reason(reason)
                .relatedOrderId(refId)
                .build();

        txRepo.save(tx);

        // 트랜잭션 기록 후 실제 재고 수량 동기화
        int net = currentNet(opt.getId());
        opt.updateStockQty(net); // 변경 감지로 flush

        return tx;
    }

    private int currentNet(Long optionId) {
        return txRepo.calcNetQty(optionId);
    }

    private void assertPositive(int qty, String msg) {
        if (qty <= 0) throw new IllegalArgumentException(msg);
    }

    public void setQty(long ownerId, long optionId, int newQty) {
        if (newQty < 0) throw new IllegalArgumentException("수량은 0 이상이어야 합니다.");

        // 1) 소유자 검증
        boolean owns = optionRepo.existsByIdAndProductStoreOwnerId(optionId, ownerId);
        if (!owns) throw new AccessDeniedException("권한이 없습니다.");

        // 2) 현재 순재고(null 안전)
        Integer cur = currentNet(optionId); // calcNetQty 가 COALESCE 되어 있으면 null 아님
        int current = (cur == null ? 0 : cur);

        // 3) 맞춰야 할 증감치
        int diff = newQty - current;
        if (diff == 0) return; // 이미 동일

        // 4) 트랜잭션 기록 남기며 절대값 맞추기
        if (diff > 0) {
            in(optionId, diff, "수동 설정(+)");
        } else {
            out(optionId, -diff, "수동 설정(-)", null);
        }
    }
}
