package com.se104.passbookapp.data.remote.interceptor

import com.se104.passbookapp.data.dto.response.TokenResponse
import com.se104.passbookapp.data.remote.api.AuthApiService
import com.se104.passbookapp.di.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class AuthAuthenticator(
    private val tokenManager: TokenManager,
    private val authApiService: AuthApiService
) : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking{ // Prevent multiple refresh calls

            val currentAccessToken = tokenManager.getAccessToken().first()
            val refreshToken = tokenManager.getRefreshToken().first()
            // If the access token changed since the first failed request, retry with new token
            if (currentAccessToken != response.request.header("Authorization")?.removePrefix("Bearer ")) {
                return@runBlocking response.request.newBuilder()
                    .header("Authorization", "Bearer $currentAccessToken")
                    .build()
            }

            val newTokensResponse = getNewToken(refreshToken)
            if (!newTokensResponse.isSuccessful || newTokensResponse.body() == null) {
                tokenManager.deleteToken()
                return@runBlocking null
            }
            val newTokens = newTokensResponse.body()
            newTokens?.let {
                tokenManager.saveToken(newTokens.accessToken, newTokens.refreshToken)

                return@runBlocking response.request.newBuilder()
                    .header("Authorization", "Bearer ${newTokens.accessToken}")
                    .build()
            }

        }
    }
    private suspend fun getNewToken(refreshToken: String?): retrofit2.Response<TokenResponse> {
        return authApiService.refreshToken(refreshToken)
    }
}