package me.bttf.smartstore.domain.common;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@MappedSuperclass
@NoArgsConstructor
@Getter
public class BaseEntity {

    @Id @GeneratedValue
    private Long id;

    @Column(updatable = false)
    private LocalDateTime createdAt =  LocalDateTime.now();
}
