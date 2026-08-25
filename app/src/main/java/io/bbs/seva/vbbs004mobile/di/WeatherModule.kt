package io.bbs.seva.vbbs004mobile.di

// di/WeatherModule.kt
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.bbs.seva.vbbs004mobile.data.repository.WeatherRepositoryImpl
import io.bbs.seva.vbbs004mobile.domain.repository.WeatherRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherModule {

    @Binds
    @Singleton
    abstract fun bindWeatherRepository(
        weatherRepositoryImpl: WeatherRepositoryImpl
    ): WeatherRepository
}
