package me.bttf.smartstore.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Embeddable
@NoArgsConstructor
@Getter
public class Money {
    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    public Money(BigDecimal amount){
        this.amount = amount;
    }

    public static Money of(long value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money of(double value) {
        return new Money(BigDecimal.valueOf(value));
    }
}
