package com.se104.passbookapp.data.di

import com.se104.passbookapp.data.remote.api.AuthApiService
import com.se104.passbookapp.data.repository.AuthRepoImpl
import com.se104.passbookapp.data.repository.SalesReportRepoImpl
import com.se104.passbookapp.data.repository.SavingTicketRepoImpl
import com.se104.passbookapp.data.repository.TransactionRepoImpl
import com.se104.passbookapp.data.repository.WithdrawalRepoImpl
import com.se104.passbookapp.domain.repository.AuthRepository
import com.se104.passbookapp.domain.repository.SalesReportRepository
import com.se104.passbookapp.domain.repository.SavingTicketRepository
import com.se104.passbookapp.domain.repository.TransactionRepository
import com.se104.passbookapp.domain.repository.WithdrawalRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class MainDataModule {
    @Binds
    abstract fun provideAuthRepository(impl: AuthRepoImpl): AuthRepository

    @Binds
    abstract fun provideSavingTicketRepository(impl: SavingTicketRepoImpl): SavingTicketRepository

    @Binds
    abstract fun provideTransactionRepository(impl: TransactionRepoImpl): TransactionRepository

    @Binds
    abstract fun provideSalesReportRepository(impl: SalesReportRepoImpl): SalesReportRepository

    @Binds
    abstract fun provideWithdrawalRepository(impl: WithdrawalRepoImpl): WithdrawalRepository
}