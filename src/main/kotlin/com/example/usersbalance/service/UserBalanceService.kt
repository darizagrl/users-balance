package com.example.usersbalance.service

import com.example.usersbalance.persistence.entity.Balance
import com.example.usersbalance.persistence.entity.User
import com.example.usersbalance.persistence.repository.BalanceRepository
import com.example.usersbalance.persistence.repository.UserRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserBalanceService(
    private val balanceRepository: BalanceRepository,
    private val userRepository: UserRepository
) {

    @Transactional
    fun setUsersBalance(balances: Map<UUID, Int>) {
        val userIds = balances.keys.toSet()
        val existingUsersMap = getExistingUsers(userIds)
        insertNewUsers(userIds, existingUsersMap)
        updateBalances(balances)
    }

    private fun getExistingUsers(userIds: Set<UUID>): Map<UUID, User> {
        val existingUsers = userRepository.findUsersById(userIds)
        return existingUsers.associateBy { it.id }
    }

    private fun insertNewUsers(userIds: Set<UUID>, existingUsersMap: Map<UUID, User>) {
        val usersToInsert = userIds.filterNot { existingUsersMap.containsKey(it) }.map { User(id = it) }
        userRepository.saveAll(usersToInsert)
    }

    private fun updateBalances(balances: Map<UUID, Int>) {
        val existingBalances = balanceRepository.findByUserIds(balances.keys)
        val existingBalancesMap = existingBalances.associateBy { it.userId }

        val balanceEntities = balances.map { (userId, balanceValue) ->
            existingBalancesMap[userId]?.apply {
                balance = balanceValue
            } ?: Balance(userId = userId, balance = balanceValue)
        }

        balanceRepository.saveAll(balanceEntities)
    }

}