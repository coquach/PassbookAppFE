package com.se104.passbookapp.domain.use_case.user

import com.se104.passbookapp.data.dto.filter.UserFilter
import com.se104.passbookapp.domain.repository.UserRepository
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(filter: UserFilter) = userRepository.getUsers(filter)
}