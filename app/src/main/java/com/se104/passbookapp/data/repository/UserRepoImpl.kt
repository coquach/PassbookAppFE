package com.se104.passbookapp.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.se104.passbookapp.data.dto.ApiResponse
import com.se104.passbookapp.data.dto.apiRequestFlow
import com.se104.passbookapp.data.dto.filter.UserFilter
import com.se104.passbookapp.data.model.User
import com.se104.passbookapp.data.paging.TransactionPagingSource
import com.se104.passbookapp.data.paging.UserPagingSource
import com.se104.passbookapp.data.remote.api.UserApiService
import com.se104.passbookapp.domain.repository.UserRepository
import com.se104.passbookapp.utils.Constants.ITEMS_PER_PAGE
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class UserRepoImpl @Inject constructor(
    private val userApiService: UserApiService,
) : UserRepository {
    override fun getMyInfo(): Flow<ApiResponse<User>> {
        return apiRequestFlow {
            userApiService.getMyInfo()
        }
    }

    override fun getUsers(filter: UserFilter): Flow<PagingData<User>> {
        return Pager(
            config = PagingConfig(
                pageSize = ITEMS_PER_PAGE,
                initialLoadSize = ITEMS_PER_PAGE,
                prefetchDistance = 2,
                enablePlaceholders = true
            ),
            pagingSourceFactory = {
                UserPagingSource(userApiService, filter)
            }).flow
    }

    override fun setActiveUser(
        userId: Long,
        isActive: Boolean,
    ): Flow<ApiResponse<Unit>> {
        return apiRequestFlow { userApiService.setActiveUser(userId, isActive) }
    }
}