package com.se104.passbookapp.domain.repository

import androidx.paging.PagingData
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.filter.SavingTicketFilter
import com.se104.passbookapp.data.dto.request.SavingTicketRequest
import com.se104.passbookapp.data.model.SavingTicket
import kotlinx.coroutines.flow.Flow

interface SavingTicketRepository {
    fun getSavingTickets(filter: SavingTicketFilter): Flow<PagingData<SavingTicket>>
    fun createSavingTicket(request: SavingTicketRequest): Flow<ApiResponse<SavingTicket>>
    fun setSavingTicketActive(id: Long, isActive: Boolean): Flow<ApiResponse<Unit>>

}