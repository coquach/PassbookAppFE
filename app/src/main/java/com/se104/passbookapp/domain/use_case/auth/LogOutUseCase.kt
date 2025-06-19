package com.se104.passbookapp.domain.use_case.auth

import com.se104.passbookapp.data.datastore.UserSessionRepository
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.di.TokenManager
import com.se104.passbookapp.domain.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class LogOutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager,
    private val userSessionRepository: UserSessionRepository
) {
    operator fun invoke() = flow<ApiResponse<Unit>> {
        try {
            val tokenAccess = tokenManager.getAccessToken().first()
            authRepository.logout(tokenAccess).collect {
               when(it){
                   is ApiResponse.Success -> {
                       tokenManager.deleteToken()
                       userSessionRepository.clear()
                       emit(ApiResponse.Success(Unit))
                   }
                   is ApiResponse.Failure -> {
                       emit(ApiResponse.Failure(it.errorMessage, it.code))
                   }
                   is ApiResponse.Loading -> {
                       emit(ApiResponse.Loading)
                   }
               }
            }
        }catch (e: Exception){
            emit(ApiResponse.Failure("Đã có lỗi xảy ra đăng xuất", 999))
        }
    }.flowOn(Dispatchers.IO)
}