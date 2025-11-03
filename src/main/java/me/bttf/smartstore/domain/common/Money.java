package me.bttf.smartstore.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Embeddable
@NoArgsConstructor
@Getter
public class Money {

    @Column(name = "amount", precision = 14, scale = 2, nullable = false)
    private BigDecimal amount;

    private Money(BigDecimal amount) {
        this.amount = normalize(amount);
    }

    private static BigDecimal normalize(BigDecimal v) {
        return v.setScale(2, RoundingMode.HALF_UP);
    }

    /** BigDecimal 지원 팩토리 */
    public static Money of(BigDecimal amount) {
        if (amount == null) throw new IllegalArgumentException("amount must not be null");
        return new Money(amount);
    }

    /** 정수 금액 지원 */
    public static Money of(long amount) {
        return new Money(BigDecimal.valueOf(amount));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return Objects.equals(amount, money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public String toString() {
        return amount.toPlainString();
    }
}
