package com.se104.passbookapp.data.di

import com.se104.passbookapp.data.repository.SavingTypeRepoImpl
import com.se104.passbookapp.domain.repository.SavingTypeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun provideSavingTypeRepository(impl: SavingTypeRepoImpl): SavingTypeRepository
}