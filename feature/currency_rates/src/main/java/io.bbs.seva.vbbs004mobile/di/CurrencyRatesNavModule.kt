package io.bbs.seva.vbbs004mobile.di
// feature/currency_rates/src/main/java/io.bbs.seva.vbbs004mobile/di/CurrencyRatesNavModule.kt

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.multibindings.IntoSet
import io.bbs.seva.vbbs004mobile.presentation.navigation.AppNavigator
import io.bbs.seva.vbbs004mobile.presentation.navigation.currencyRatesEntryBuilder

@Module
@InstallIn(ActivityRetainedComponent::class)
object CurrencyRatesNavModule {

    @IntoSet
    @Provides
    fun currencyRatesEntries(): EntryProviderScope<NavKey>.(AppNavigator) -> Unit = {
        currencyRatesEntryBuilder(it)
    }
}
