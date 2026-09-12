package io.bbs.seva.vbbs004mobile.presentation.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.bbs.seva.vbbs004mobile.presentation.screens.home.HomeScreen
import io.bbs.seva.vbbs004mobile.presentation.screens.home.HomeViewModel

fun EntryProviderScope<NavKey>.homeEntryBuilder(navigator: AppNavigator) {
    entry<Destination.Home> {
        val viewModel: HomeViewModel = hiltViewModel()
        HomeScreen(
            viewModel = viewModel,
            // ← copy the EXACT lambdas from AppRoot's current Home entry,
            //   replacing backstack.add(X) with navigator.navigate(X)
            //   and backstack.removeAt(lastIndex) with navigator.back()
        )
    }
}