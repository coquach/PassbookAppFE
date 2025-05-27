package com.se104.passbookapp.data.repository

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.apiRequestFlow
import com.se104.passbookapp.data.dto.request.WithdrawalTicketRequest
import com.se104.passbookapp.data.model.WithdrawalTicket
import com.se104.passbookapp.data.remote.api.WithdrawalApiService
import com.se104.passbookapp.domain.repository.WithdrawalRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WithdrawalRepoImpl @Inject constructor(
    private val withdrawalApiService: WithdrawalApiService,
) : WithdrawalRepository {
    override fun createWithdrawalTicket(request: WithdrawalTicketRequest): Flow<ApiResponse<WithdrawalTicket>> {
        return apiRequestFlow {
            withdrawalApiService.createWithdrawalTicket(request)
        }
    }
}