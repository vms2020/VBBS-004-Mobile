package io.bbs.seva.vbbs004mobile.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.bbs.seva.vbbs004mobile.data.location.DefaultGeoLocationTracker
import io.bbs.seva.vbbs004mobile.domain.location.GeoLocationTracker
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    @Binds
    @Singleton
    abstract fun bindLocationTracker(
        impl: DefaultGeoLocationTracker
    ): GeoLocationTracker

}