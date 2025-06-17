package com.se104.passbookapp.domain.use_case.saving_ticket

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.SavingTicketRequest
import com.se104.passbookapp.data.model.SavingTicket
import com.se104.passbookapp.domain.repository.SavingTicketRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.math.BigDecimal
import javax.inject.Inject

class CreateSavingTicketUseCase @Inject constructor(
    private val savingTicketRepository: SavingTicketRepository,
) {
    operator fun invoke(userId: Long, savingTypeId: Long, amount: BigDecimal) =
        flow<ApiResponse<SavingTicket>> {
            try {
                emit(ApiResponse.Loading)
                val request = SavingTicketRequest(userId, savingTypeId, amount)
                savingTicketRepository.createSavingTicket(request).collect {

                    emit(it)

                }
            } catch (e: Exception) {
                emit(ApiResponse.Failure("Đã xảy ra lỗi khi tạo phiếu tiết kiệm", 999))
            }
        }.flowOn(Dispatchers.IO)
}