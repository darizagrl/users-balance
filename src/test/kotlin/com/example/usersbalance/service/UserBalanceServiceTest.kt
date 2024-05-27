package com.example.usersbalance.service

import com.example.usersbalance.persistence.entity.Balance
import com.example.usersbalance.persistence.entity.User
import com.example.usersbalance.persistence.repository.BalanceRepository
import com.example.usersbalance.persistence.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.util.*

@ExtendWith(MockitoExtension::class)
class UserBalanceServiceTest {

    @Mock
    private lateinit var balanceRepository: BalanceRepository

    @Mock
    private lateinit var userRepository: UserRepository

    @InjectMocks
    private lateinit var userBalanceService: UserBalanceService

    @Captor
    private lateinit var userCaptor: ArgumentCaptor<List<User>>

    @Captor
    private lateinit var balanceCaptor: ArgumentCaptor<List<Balance>>

    private lateinit var userId1: UUID
    private lateinit var userId2: UUID
    private lateinit var balanceMap: Map<UUID, Int>

    @BeforeEach
    fun setUp() {
        userId1 = UUID.randomUUID()
        userId2 = UUID.randomUUID()
        balanceMap = mapOf(userId1 to 100, userId2 to 200)
    }

    @Test
    fun givenNewUser_whenSetUsersBalance_thenInsertNewUsersAndBalances() {
        // given
        whenever(userRepository.findUsersById(setOf(userId1, userId2))).thenReturn(emptyList())
        whenever(balanceRepository.findByUserIds(setOf(userId1, userId2))).thenReturn(emptyList())

        // when
        userBalanceService.setUsersBalance(balanceMap)

        // then
        verify(userRepository).saveAll(userCaptor.capture())
        val savedUsers = userCaptor.value
        assertThat(savedUsers).hasSize(2).extracting("id").containsExactly(userId1, userId2)

        verify(balanceRepository).saveAll(balanceCaptor.capture())
        val savedBalances = balanceCaptor.value
        assertThat(savedBalances).hasSize(2).extracting("userId", "balance")
            .containsExactly(tuple(userId1, 100), tuple(userId2, 200))
    }

    @Test
    fun givenExistingUser_whenSetUsersBalance_thenUpdateBalance() {
        // given
        val existingUser = User(id = userId1, name = "User 1")
        val existingBalance = Balance(userId = userId1, balance = 50)

        whenever(userRepository.findUsersById(setOf(userId1, userId2))).thenReturn(listOf(existingUser))
        whenever(balanceRepository.findByUserIds(setOf(userId1, userId2))).thenReturn(listOf(existingBalance))

        // when
        userBalanceService.setUsersBalance(balanceMap)

        //then
        verify(userRepository).saveAll(userCaptor.capture())
        val savedUsers = userCaptor.value
        assertThat(savedUsers).hasSize(1).extracting("id").containsExactly(userId2)

        verify(balanceRepository).saveAll(balanceCaptor.capture())
        val savedBalances = balanceCaptor.value
        assertThat(savedBalances).hasSize(2).extracting("userId", "balance")
            .containsExactly(tuple(userId1, 100), tuple(userId2, 200))
    }
}
