package com.harry.data.di

import com.harry.data.repository.ObjectDetectionResultRepositoryImpl
import com.harry.data.usecase.ObjectDetectUseCaseImpl
import com.harry.domain.repository.ObjectDetectionResultRepository
import com.harry.domain.usecase.ObjectDetectUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DiModule {

    // UseCase 바인딩
    @Binds
    abstract fun bindObjectDetectUseCase(
        objectDetectUseCaseImpl: ObjectDetectUseCaseImpl
    ): ObjectDetectUseCase

    // Repository 바인딩
    @Binds
    abstract fun bindObjectDetectionResultRepository(
        objectDetectionResultRepositoryImpl: ObjectDetectionResultRepositoryImpl
    ): ObjectDetectionResultRepository
}