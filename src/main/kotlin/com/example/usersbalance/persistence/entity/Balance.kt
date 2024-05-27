package com.example.usersbalance.persistence.entity

import jakarta.persistence.*
import org.hibernate.annotations.CacheConcurrencyStrategy
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "balances")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
class Balance(
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private val id: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    val userId: UUID = UUID.randomUUID(),

    @Column(nullable = false)
    var balance: Int = 0,

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
) : AbstractEntity<UUID>() {

    override fun getId(): UUID {
        return id
    }
}