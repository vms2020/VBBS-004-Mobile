package io.bbs.seva.vbbs004mobile.presentation.navigation

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.bbs.seva.vbbs004mobile.presentation.screens.settings.SettingsScreen

fun EntryProviderScope<NavKey>.settingsEntryBuilder(navigator: AppNavigator) {
    entry<Destination.SettingsDest> {
        SettingsScreen()
    }
}
