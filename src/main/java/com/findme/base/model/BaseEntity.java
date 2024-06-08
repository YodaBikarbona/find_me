package com.findme.base.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@MappedSuperclass
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private Long id;

    @Column(updatable = false)
    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    private Instant createdAt;

    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
    private Instant modifiedAt;

    public Long getId() {
        return id;
    }
}
