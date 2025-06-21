package com.se104.passbookapp.data.repository

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.apiRequestFlow
import com.se104.passbookapp.data.dto.request.GroupRequest
import com.se104.passbookapp.data.model.Group
import com.se104.passbookapp.data.model.Permission
import com.se104.passbookapp.data.remote.GroupApiService
import com.se104.passbookapp.domain.repository.GroupRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GroupRepoImpl @Inject constructor(
    private val groupApiService: GroupApiService
) : GroupRepository {
    override fun getPermissions(): Flow<ApiResponse<List<Permission>>> {
        return apiRequestFlow {
            groupApiService.getPermissions()
        }
    }

    override fun getGroups(): Flow<ApiResponse<List<Group>>> {
        return apiRequestFlow {
            groupApiService.getGroups()
        }
    }

    override fun createGroup(request: GroupRequest): Flow<ApiResponse<Group>> {
        return apiRequestFlow {
            groupApiService.createGroup(request)
        }
    }

    override fun updateGroup(
        request: GroupRequest,
        id: Int,
    ): Flow<ApiResponse<Group>> {
        return apiRequestFlow {
            groupApiService.updateGroup(request, id)
        }
    }

    override fun deleteGroup(id: Int): Flow<ApiResponse<Unit>> {
        return apiRequestFlow {
            groupApiService.deleteGroup(id)
        }
    }


}