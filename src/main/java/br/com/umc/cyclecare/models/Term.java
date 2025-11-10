package br.com.umc.cyclecare.models;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;

@Entity
@Table(name = "terms")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Term extends DomainEntity {
    @Column(nullable = false, columnDefinition = "TEXT")
    private String terms;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
