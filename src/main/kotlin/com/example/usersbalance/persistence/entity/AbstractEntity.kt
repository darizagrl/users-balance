package com.example.usersbalance.persistence.entity

import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PostLoad
import jakarta.persistence.PrePersist
import org.springframework.data.domain.Persistable

@MappedSuperclass
abstract class AbstractEntity<ID> : Persistable<ID> {

    @Transient
    private var isNew = true

    override fun isNew(): Boolean {
        return isNew
    }

    @PrePersist
    @PostLoad
    fun markNotNew() {
        isNew = false
    }
}
