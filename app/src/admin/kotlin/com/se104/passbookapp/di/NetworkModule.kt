package com.se104.passbookapp.di

import com.se104.passbookapp.data.remote.GroupApiService
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
    fun provideGroupApiService(
        @Named("MainApi") retrofit: Retrofit,
    ): GroupApiService =
        retrofit
            .create(GroupApiService::class.java)




}