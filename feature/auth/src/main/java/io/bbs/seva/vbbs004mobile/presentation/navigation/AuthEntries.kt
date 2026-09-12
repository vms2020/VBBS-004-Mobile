// feature/auth/.../presentation/navigation/AuthEntries.kt
package io.bbs.seva.vbbs004mobile.presentation.navigation

import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel   // same import AppRoot uses
import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavKey
import io.bbs.seva.vbbs004mobile.presentation.screens.login.LoginScreen
import io.bbs.seva.vbbs004mobile.presentation.screens.login.LoginViewModel
import io.bbs.seva.vbbs004mobile.presentation.screens.signup.SignupScreen
import io.bbs.seva.vbbs004mobile.presentation.screens.signup.SignupViewModel

// receiver <NavKey>, `entry` resolves through it — no import, same as AppRoot
fun EntryProviderScope<NavKey>.authEntryBuilder(navigator: AppNavigator) {
    entry<Destination.Login> {
        val viewModel: LoginViewModel = hiltViewModel()
        LoginScreen(
            viewModel = viewModel,
            onNavigateToHome = { navigator.navigate(Destination.Home) },
            onNavigateToSignup = { navigator.navigate(Destination.Signup) },
        )
    }
    entry<Destination.Signup> {
        val viewModel: SignupViewModel = hiltViewModel()
        SignupScreen(
            viewModel = viewModel,
            onNavigateToLogin = { navigator.back() },
            onNavigateToHome = { navigator.navigate(Destination.Home) },
        )
    }
}
