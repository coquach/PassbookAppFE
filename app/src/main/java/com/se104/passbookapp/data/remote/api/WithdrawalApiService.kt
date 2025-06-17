package com.se104.passbookapp.data.remote.api

import com.se104.passbookapp.data.dto.request.WithdrawalTicketRequest
import com.se104.passbookapp.data.model.WithdrawalTicket
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface WithdrawalApiService {
    companion object {
        const val PR = "withdrawal-tickets"
    }

    @POST(PR)
    suspend fun createWithdrawalTicket(@Body request: WithdrawalTicketRequest): Response<WithdrawalTicket>
}