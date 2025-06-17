package com.se104.passbookapp.data.paging

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.apiRequestFlow
import com.se104.passbookapp.data.dto.filter.SavingTicketFilter
import com.se104.passbookapp.data.dto.response.PageResponse
import com.se104.passbookapp.data.model.SavingTicket
import com.se104.passbookapp.data.remote.api.SavingTicketApiService
import com.se104.passbookapp.utils.StringUtils
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SavingTicketPagingSource @Inject constructor(
    private val savingTicketApiService: SavingTicketApiService,
    private val filter: SavingTicketFilter,
) : ApiPagingSource<SavingTicket>() {
    override suspend fun fetch(
        page: Int,
        size: Int,
    ): Flow<ApiResponse<PageResponse<SavingTicket>>> {
        return apiRequestFlow {

                savingTicketApiService.getSavingTickets(
                    page = page,
                    size = size,
                    userId = filter.userId,
                    savingTypeId = filter.savingTypeId,
                    isActive = filter.isActive,
                    order = filter.order,
                    sortBy = filter.sortBy,
                    startDate =StringUtils.formatLocalDate(filter.startDate),
                    endDate = StringUtils.formatLocalDate(filter.endDate),

                )

        }
    }
}