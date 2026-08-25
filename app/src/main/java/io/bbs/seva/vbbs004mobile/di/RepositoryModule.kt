package io.bbs.seva.vbbs004mobile.di
// di/RepositoryModule.kt

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.bbs.seva.vbbs004mobile.data.repository.AuthRepositoryImpl
import io.bbs.seva.vbbs004mobile.domain.repository.AuthRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository
}
