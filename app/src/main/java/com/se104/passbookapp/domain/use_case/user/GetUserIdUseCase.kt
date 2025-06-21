package com.se104.passbookapp.domain.use_case.user

import com.se104.passbookapp.data.datastore.UserSessionRepository
import javax.inject.Inject

class GetUserIdUseCase @Inject constructor(
    private val userSessionRepository: UserSessionRepository
) {
    suspend  operator fun invoke(): Long? {
        return userSessionRepository.getUserId()
    }
}