package io.bbs.seva.vbbs004mobile.di

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import io.bbs.seva.vbbs004mobile.presentation.navigation.AppNavigator
import io.bbs.seva.vbbs004mobile.presentation.navigation.editProfileEntryBuilder


@Module
@InstallIn(ActivityRetainedComponent::class)
object EditProfileNavModule {
    @IntoSet
    @Provides
    fun editProfileEntries(): EntryProviderScope<NavKey>.(AppNavigator) -> Unit = {
        editProfileEntryBuilder(it)
    }
}
