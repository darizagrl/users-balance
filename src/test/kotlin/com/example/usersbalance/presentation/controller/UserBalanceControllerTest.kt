package com.example.usersbalance.presentation.controller

import com.example.usersbalance.service.UserBalanceService
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.util.*

@ExtendWith(MockitoExtension::class)
@WebMvcTest(UserBalanceController::class)
class UserBalanceControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var userBalanceService: UserBalanceService

    @Test
    fun givenBalances_whenUpdateUsersBalances_thenSetBalanceAndReturn204() {
        // given
        val balances = mapOf(
            UUID.randomUUID() to 100,
            UUID.randomUUID() to 200,
            UUID.randomUUID() to 300
        )

        // when-then
        mockMvc.perform(
            MockMvcRequestBuilders.put("/users/balances")
                .contentType(MediaType.APPLICATION_JSON)
                .content(balances.toJson())
        ).andExpect(status().isNoContent)

        verify(userBalanceService).setUsersBalance(balances)
    }

    private fun Any.toJson(): String = ObjectMapper().writeValueAsString(this)
}