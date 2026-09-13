package io.bbs.seva.vbbs004mobile.di

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import io.bbs.seva.vbbs004mobile.presentation.navigation.AppNavigator
import io.bbs.seva.vbbs004mobile.presentation.navigation.locationEntryBuilder

@Module
@InstallIn(ActivityRetainedComponent::class)
object LocationNavModule {
    @IntoSet
    @Provides
    fun locationEntry(): EntryProviderScope<NavKey>.(AppNavigator) -> Unit = {
        locationEntryBuilder(it)
    }
}
