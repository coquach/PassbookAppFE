package com.se104.passbookapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.apiRequestFlow
import com.se104.passbookapp.data.dto.filter.SavingTicketFilter
import com.se104.passbookapp.data.dto.request.SavingTicketRequest
import com.se104.passbookapp.data.model.SavingTicket
import com.se104.passbookapp.data.paging.SavingTicketPagingSource
import com.se104.passbookapp.data.remote.api.SavingTicketApiService
import com.se104.passbookapp.domain.repository.SavingTicketRepository
import com.se104.passbookapp.utils.Constants.ITEMS_PER_PAGE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavingTicketRepoImpl @Inject constructor(
    private val savingTicketApiService: SavingTicketApiService,
) : SavingTicketRepository {
    override fun getSavingTickets(filter: SavingTicketFilter): Flow<PagingData<SavingTicket>> {
        return Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                initialLoadSize = ITEMS_PER_PAGE,
                prefetchDistance = 2,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                SavingTicketPagingSource(savingTicketApiService, filter, false)
            }
        ).flow
    }

    override fun getSavingTicketsForCustomer(filter: SavingTicketFilter): Flow<PagingData<SavingTicket>> {
        return Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                initialLoadSize = ITEMS_PER_PAGE,
                prefetchDistance = 2,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                SavingTicketPagingSource(savingTicketApiService, filter, true)
            }
        ).flow
    }


    override fun createSavingTicket(request: SavingTicketRequest): Flow<ApiResponse<SavingTicket>> {
        return apiRequestFlow {
            savingTicketApiService.createSavingTicket(request)
        }
    }

}