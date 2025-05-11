package com.se104.passbookapp.data.remote.interceptor

import com.se104.passbookapp.BuildConfig
import com.se104.passbookapp.data.dto.response.TokenResponse
import com.se104.passbookapp.data.remote.api.AuthApiService
import com.se104.passbookapp.di.TokenManager
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AuthAuthenticator(
    private val tokenManager: TokenManager,
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
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val okHttpClient = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()

        val retrofit = Retrofit.Builder()
            .baseUrl(BuildConfig.BACKEND_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
        val service = retrofit.create(AuthApiService::class.java)
        return service.refreshToken(refreshToken)
    }
}