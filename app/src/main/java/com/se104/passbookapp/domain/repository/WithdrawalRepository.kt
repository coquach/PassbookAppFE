package com.se104.passbookapp.domain.repository

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.WithdrawalTicketRequest
import com.se104.passbookapp.data.model.WithdrawalTicket
import kotlinx.coroutines.flow.Flow

interface WithdrawalRepository {
    fun createWithdrawalTicket(request: WithdrawalTicketRequest): Flow<ApiResponse<WithdrawalTicket>>
}