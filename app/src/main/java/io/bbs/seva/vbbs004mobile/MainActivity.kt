package io.bbs.seva.vbbs004mobile

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import dagger.hilt.android.AndroidEntryPoint
import io.bbs.seva.vbbs004mobile.domain.repository.AuthRepository
import io.bbs.seva.vbbs004mobile.presentation.home.HomeScreen
import io.bbs.seva.vbbs004mobile.presentation.home.HomeViewModel
import io.bbs.seva.vbbs004mobile.presentation.login.LoginScreen
import io.bbs.seva.vbbs004mobile.presentation.login.LoginViewModel
import io.bbs.seva.vbbs004mobile.presentation.navigation.Destination
import io.bbs.seva.vbbs004mobile.presentation.weather.WeatherScreen
import io.bbs.seva.vbbs004mobile.ui.theme.Vbbs004MobileTheme
import javax.inject.Inject

private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Vbbs004MobileTheme {
                // Collect the authentication state asynchronously
                val isAuthenticatedState = authRepository.isAuthenticated
                    .collectAsState(initial = null)

                val isAuthenticated = isAuthenticatedState.value

                // 1. Show nothing or a Splash/Loading screen while reading disk
                if (isAuthenticated == null) {
                    // You can place a Box with a CircularProgressIndicator here
                    Surface {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                        Log.i(
                            TAG,
                            "onCreate: !!!!!!!!!!!!!!!!! isAuthenticated == null !!!!!!!!!!!!!!!!!!!!!!"
                        )
                    }
                    return@Vbbs004MobileTheme
                }

                // 2. Once loaded, determine your starting point cleanly
                val startDestination = if (isAuthenticated) {
                    Destination.Home
                } else {
                    Destination.Login
                }

                // Your developer-owned backstack list
                val backstack = rememberNavBackStack(startDestination)

                // Map your destinations using the base NavKey generic definition
                val myEntryProvider = remember {
                    entryProvider<NavKey> {
                        entry<Destination.Login> {
                            val viewModel: LoginViewModel = hiltViewModel()
                            LoginScreen(
                                viewModel = viewModel,
                                onNavigateToHome = { backstack.add(Destination.Home) }
                            )
                        }
                        entry<Destination.Home> {
                            val viewModel: HomeViewModel = hiltViewModel()
                            HomeScreen(
                                onBack = {
                                    if (backstack.size > 1) backstack.removeAt(backstack.lastIndex)
                                },
                                onNavigateToWeather = { backstack.add(Destination.Weather) },
                                viewModel = viewModel,
                            )
                        }
                        entry<Destination.Weather> {
                            WeatherScreen(
                                onBack = {
                                    if (backstack.size > 1) backstack.removeAt(backstack.lastIndex)
                                },
                            )
                        }
                    }
                }

                // The correct, verified NavDisplay execution matching types completely
                NavDisplay(
                    backStack = backstack,
                    onBack = {
                        if (backstack.size > 1) {
                            backstack.removeAt(backstack.lastIndex)
                        } else {
                            finish()
                        }
                    },
                    entryProvider = myEntryProvider
                )
            }
        }
    }
}
