package com.se104.passbookapp.domain.repository

import androidx.paging.PagingData
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.filter.UserFilter
import com.se104.passbookapp.data.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getMyInfo(): Flow<ApiResponse<User>>
    fun getUsers(filter: UserFilter): Flow<PagingData<User>>
    fun setActiveUser(userId: Long, isActive: Boolean): Flow<ApiResponse<Unit>>
    fun setGroupUser(userId: Long, groupId: Int): Flow<ApiResponse<Unit>>
    fun changePassword(request: Map<String, String>): Flow<ApiResponse<Unit>>

}