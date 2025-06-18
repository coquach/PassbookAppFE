package com.se104.passbookapp.data.remote.api

import com.se104.passbookapp.data.dto.response.PageResponse
import com.se104.passbookapp.data.model.Transaction
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.math.BigDecimal

interface TransactionApiService {
    companion object{
        const val PR = "transactions"
    }

    @GET(PR)
    suspend fun getTransactions(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "id",
        @Query("order") order: String = "asc",
        @Query("citizenId") citizenId: String? = null,
        @Query("transactionType") transactionType: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,
    ): Response<PageResponse<Transaction>>

    @GET("$PR/customer")
    suspend fun getTransactionsForCustomer(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 10,
        @Query("sortBy") sortBy: String = "id",
        @Query("order") order: String = "asc",
        @Query("citizenId") citizenId: String? = null,
        @Query("transactionType") transactionType: String? = null,
        @Query("startDate") startDate: String? = null,
        @Query("endDate") endDate: String? = null,

    ) : Response<PageResponse<Transaction>>

    @POST("$PR/deposit")
    suspend fun deposit(@Body userId: Long, @Body amount: BigDecimal) : Response<Transaction>

    @POST("$PR/withdraw")
    suspend fun withdraw(@Body userId: Long, @Body amount: BigDecimal) : Response<Transaction>
}