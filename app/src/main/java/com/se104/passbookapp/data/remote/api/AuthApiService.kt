package com.se104.passbookapp.data.remote.api

import com.se104.passbookapp.data.dto.request.LoginRequest
import com.se104.passbookapp.data.dto.request.RegisterRequest
import com.se104.passbookapp.data.dto.response.TokenResponse
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.Header
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
        @Header("refresh-token") tokenRefresh: String?= null
    ) : Response<TokenResponse>

    //Logout

    @POST("$PR/logout")
    suspend fun logout(
        @Header("access-token") tokenAccess: String?= null,
    ) : Response<Unit>

    @POST("$PR/forgot-password")
    suspend fun forgotPassword(
        @Body request: Map<String, String>
    ) : Response<Unit>


    @POST("$PR/change-password")
    suspend fun changePassword(
        @Body request: Map<String, String>
    ) : Response<Unit>


    @POST("$PR/send-otp")
    suspend fun verifyEmail(
        @Body request: Map<String, String>
    ) : Response<Unit>

    @POST("$PR/verify")
    suspend fun postMethodName(
        @Body request: Map<String, String>
    ) : Response<Unit>




}