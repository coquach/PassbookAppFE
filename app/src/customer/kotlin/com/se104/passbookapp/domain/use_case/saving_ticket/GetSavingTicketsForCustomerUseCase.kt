package com.se104.passbookapp.domain.use_case.saving_ticket

import com.se104.passbookapp.data.dto.filter.SavingTicketFilter
import com.se104.passbookapp.domain.repository.SavingTicketRepository
import javax.inject.Inject

class GetSavingTicketsForCustomerUseCase @Inject constructor(
    private val savingTicketRepository: SavingTicketRepository
) {
    operator fun invoke(filter: SavingTicketFilter) = savingTicketRepository.getSavingTicketsForCustomer(filter)
}