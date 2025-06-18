package com.se104.passbookapp.di

import com.se104.passbookapp.data.remote.api.ReportApiService
import com.se104.passbookapp.data.remote.api.SavingTicketApiService
import com.se104.passbookapp.data.remote.api.TransactionApiService
import com.se104.passbookapp.data.remote.api.WithdrawalApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideSavingTicketAPIService(
        @Named("MainApi") retrofit: Retrofit,
    ): SavingTicketApiService =
        retrofit
            .create(SavingTicketApiService::class.java)

    @Provides
    @Singleton
    fun provideTransactionAPIService(
        @Named("MainApi") retrofit: Retrofit,
    ): TransactionApiService =
        retrofit
            .create(TransactionApiService::class.java)

    @Provides
    @Singleton
    fun provideWithdrawalAPIService(
        @Named("MainApi") retrofit: Retrofit,
    ): WithdrawalApiService =
        retrofit
            .create(WithdrawalApiService::class.java)

    @Provides
    @Singleton
    fun provideReportAPIService(
        @Named("MainApi") retrofit: Retrofit,
    ): ReportApiService =
        retrofit
            .create(ReportApiService::class.java)
}