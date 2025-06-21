package com.se104.passbookapp.data.paging

import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.apiRequestFlow
import com.se104.passbookapp.data.dto.filter.UserFilter
import com.se104.passbookapp.data.dto.response.PageResponse
import com.se104.passbookapp.data.model.User
import com.se104.passbookapp.data.remote.api.UserApiService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserPagingSource @Inject constructor(
    private val userApiService: UserApiService,
    private val filter: UserFilter,
) : ApiPagingSource<User>() {
    override suspend fun fetch(
        page: Int,
        size: Int,
    ): Flow<ApiResponse<PageResponse<User>>> {
        return apiRequestFlow {
            userApiService.getUsers(
                page = page,
                size = size,
                fullName = filter.fullName,
                citizenID = filter.citizenID,
                groupId = filter.groupId,
                isActive = filter.isActive,
                order = filter.order,
                sortBy = filter.sortBy,
            )
        }
    }
}