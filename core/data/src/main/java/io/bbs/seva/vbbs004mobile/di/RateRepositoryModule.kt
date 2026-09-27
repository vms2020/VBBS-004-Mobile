package io.bbs.seva.vbbs004mobile.di


import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.bbs.seva.vbbs004mobile.data.repository.RateRepositoryImpl
import io.bbs.seva.vbbs004mobile.domain.repository.CurrencyRateRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RateRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRateRepository(impl: RateRepositoryImpl): CurrencyRateRepository
}