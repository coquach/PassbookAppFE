package com.se104.passbookapp.domain.repository

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.request.GroupRequest
import com.se104.passbookapp.data.model.Group
import com.se104.passbookapp.data.model.Permission
import kotlinx.coroutines.flow.Flow

interface GroupRepository {
    fun getPermissions(): Flow<ApiResponse<List<Permission>>>
    fun getGroups(): Flow<ApiResponse<List<Group>>>
    fun createGroup(request: GroupRequest): Flow<ApiResponse<Group>>
    fun updateGroup(request: GroupRequest, id: Int): Flow<ApiResponse<Group>>
    fun deleteGroup(id: Int): Flow<ApiResponse<Unit>>

}