package com.se104.passbookapp.data.remote

import com.se104.passbookapp.data.dto.request.GroupRequest
import com.se104.passbookapp.data.model.Group
import com.se104.passbookapp.data.model.Permission
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GroupApiService {

    @GET("permissions")
    suspend fun getPermissions(): Response<List<Permission>>

    @GET("groups")
    suspend fun getGroups(): Response<List<Group>>

    @POST("groups")
    suspend fun createGroup(@Body request: GroupRequest): Response<Group>

    @PUT("groups/{id}")
    suspend fun updateGroup(@Body request: GroupRequest, @Path("id") id: Int): Response<Group>

    @DELETE("groups/{id}")
    suspend fun deleteGroup(@Path("id") id: Int): Response<Unit>


}