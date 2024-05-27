package com.example.usersbalance.presentation.controller

import com.example.usersbalance.service.UserBalanceService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/users/balances")
class UserBalanceController(private val userBalanceService: UserBalanceService) {

    @PutMapping
    fun updateUsersBalances(@RequestBody balances: Map<UUID, Int>): ResponseEntity<Unit> {
        userBalanceService.setUsersBalance(balances)
        return ResponseEntity.noContent().build()
    }
}