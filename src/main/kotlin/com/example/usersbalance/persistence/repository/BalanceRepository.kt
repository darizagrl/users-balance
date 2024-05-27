package com.example.usersbalance.persistence.repository

import com.example.usersbalance.persistence.entity.Balance
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.time.LocalDateTime
import java.util.*


@Repository
interface BalanceRepository : CrudRepository<Balance, UUID> {

    @Query("SELECT b FROM Balance b WHERE b.userId IN :userIds")
    @Cacheable(value = ["balanceCache"], key = "#userIds")
    fun findByUserIds(userIds: Collection<UUID>): Collection<Balance>

    @Query("SELECT b FROM Balance b WHERE b.userId IN :userIds AND b.updatedAt BETWEEN :start AND :end")
    fun findBalancesByUserIdsAndUpdatedAtBetween(
        @Param("userIds") userIds: Collection<UUID>,
        @Param("start") start: LocalDateTime,
        @Param("end") end: LocalDateTime
    ): List<Balance>

//    @Query("SELECT b FROM Balance b WHERE b.userId >= :startUserId AND b.userId <= :endUserId")
//    fun findByUserIdRange(@Param("startUserId") startUserId: Int, @Param("endUserId") endUserId: Int): List<Balance>

}