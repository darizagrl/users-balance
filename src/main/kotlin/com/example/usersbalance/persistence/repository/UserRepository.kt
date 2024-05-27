package com.example.usersbalance.persistence.repository

import com.example.usersbalance.persistence.entity.User
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import java.util.*

@Repository
interface UserRepository : CrudRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE u.id IN :userIds")
    @Cacheable(value = ["usersCache"], key = "#userIds")
    fun findUsersById(userIds: Collection<UUID>): Collection<User>
}