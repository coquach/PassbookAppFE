package com.se104.passbookapp.data.di

import com.se104.passbookapp.data.repository.GroupRepoImpl
import com.se104.passbookapp.data.repository.ParameterRepoImpl
import com.se104.passbookapp.domain.repository.GroupRepository
import com.se104.passbookapp.domain.repository.ParameterRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {



    @Binds
    abstract fun provideGroupRepository(impl: GroupRepoImpl): GroupRepository
}