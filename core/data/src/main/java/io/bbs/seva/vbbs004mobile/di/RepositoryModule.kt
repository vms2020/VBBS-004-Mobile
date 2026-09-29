package io.bbs.seva.vbbs004mobile.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.bbs.seva.vbbs004mobile.data.repository.AppSettingsRepositoryImpl
import io.bbs.seva.vbbs004mobile.data.repository.AuthRepositoryImpl
import io.bbs.seva.vbbs004mobile.data.repository.GeoLocationRepositoryImpl
import io.bbs.seva.vbbs004mobile.data.repository.RateRepositoryImpl
import io.bbs.seva.vbbs004mobile.data.repository.WeatherRepositoryImpl
import io.bbs.seva.vbbs004mobile.domain.repository.AppSettingsRepository
import io.bbs.seva.vbbs004mobile.domain.repository.AuthRepository
import io.bbs.seva.vbbs004mobile.domain.repository.CurrencyRateRepository
import io.bbs.seva.vbbs004mobile.domain.repository.GeoLocationRepository
import io.bbs.seva.vbbs004mobile.domain.repository.WeatherRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAppSettingsRepository(
        impl: AppSettingsRepositoryImpl
    ): AppSettingsRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindGeoLocationRepository(
        impl: GeoLocationRepositoryImpl
    ): GeoLocationRepository

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository

    @Binds
    @Singleton
    abstract fun bindRateRepository(
        impl: RateRepositoryImpl
    ): CurrencyRateRepository

}