package com.se104.passbookapp.domain.use_case.user

import com.se104.passbookapp.data.datastore.UserSessionRepository
import com.se104.passbookapp.utils.Role
import javax.inject.Inject

class GetRoleUseCase @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) {
    suspend  operator fun invoke(): Role? {
        return userSessionRepository.getRole()
    }
}