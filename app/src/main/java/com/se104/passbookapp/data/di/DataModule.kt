package com.se104.passbookapp.data.di

import com.se104.passbookapp.data.repository.AuthRepoImpl
import com.se104.passbookapp.data.repository.SavingTypeRepoImpl
import com.se104.passbookapp.data.repository.UserRepoImpl
import com.se104.passbookapp.domain.repository.AuthRepository
import com.se104.passbookapp.domain.repository.SavingTypeRepository
import com.se104.passbookapp.domain.repository.UserRepository
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
    abstract fun provideSavingTypeRepository(impl: SavingTypeRepoImpl): SavingTypeRepository

   @Binds
   abstract fun provideUserRepository(impl: UserRepoImpl): UserRepository
}