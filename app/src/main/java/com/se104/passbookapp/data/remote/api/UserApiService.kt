package com.se104.passbookapp.data.remote.api

import com.se104.passbookapp.data.dto.response.PageResponse
import com.se104.passbookapp.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApiService {
    companion object{
        const val PR = "users"
    }
    @GET("$PR/my-info")
    suspend fun getMyInfo(): Response<User>

    @GET(PR)
    suspend fun getUsers(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "id",
        @Query("order") order: String = "asc",
        @Query("groupId") groupId: Int? = null,
        @Query("isActive") isActive: Boolean= true,
        @Query("fullName") fullName: String? = null,
        @Query("citizenID") citizenID: String? = null,
    ): Response<PageResponse<User>>
    

    @PUT("$PR/active/{userId}")
    suspend fun setActiveUser(@Path("userId") userId: Long, @Body isActive: Boolean): Response<Unit>


    @PATCH("$PR/{userId}/group")
    suspend fun setGroupUser(@Path("userId") userId: Long, @Body request: Map<String, Int>): Response<Unit>

    @POST("auth/change-password")
    suspend fun changePassword(
        @Body request: Map<String, String>
    ) : Response<Unit>

}