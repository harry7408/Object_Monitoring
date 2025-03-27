package com.harry.objectmonitoring

import com.harry.data.repository.ObjectRepositoryImpl
import com.harry.domain.repository.ObjectRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindObjectRepository(objectRepositoryImpl: ObjectRepositoryImpl): ObjectRepository
}