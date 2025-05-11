package com.se104.passbookapp.data.remote.api

import com.se104.passbookapp.data.dto.request.LoginRequest
import com.se104.passbookapp.data.dto.request.RegisterRequest
import com.se104.passbookapp.data.dto.response.TokenResponse
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.POST


interface AuthApiService {
    companion object {
        const val PR = "auth"
    }
    @POST("$PR/login")
    suspend fun login (
        @Body request: LoginRequest
    ) : Response<TokenResponse>

    @POST("$PR/register")
    suspend fun register(
        @Body request: RegisterRequest
    ) : Response<TokenResponse>

    @POST("$PR/refresh")
    suspend fun refreshToken(
        @Body tokenRefresh: String?= null
    ) : Response<TokenResponse>

    //Logout
}