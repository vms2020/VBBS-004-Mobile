package io.bbs.seva.vbbs004mobile.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.bbs.seva.vbbs004mobile.core.network.BuildConfig
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkConfigModule {

    @Provides
    @Singleton
    @DefaultBaseUrl
    fun provideDefaultBaseUrl(): String = BuildConfig.BASE_URL
}
