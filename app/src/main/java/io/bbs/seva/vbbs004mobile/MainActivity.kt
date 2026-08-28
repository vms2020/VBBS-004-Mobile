package io.bbs.seva.vbbs004mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import io.bbs.seva.vbbs004mobile.data.datastore.model.UserProfile
import io.bbs.seva.vbbs004mobile.di.ProfileDataStore
import io.bbs.seva.vbbs004mobile.domain.repository.AuthRepository
import io.bbs.seva.vbbs004mobile.presentation.home.HomeUiState
import io.bbs.seva.vbbs004mobile.presentation.home.HomeViewModel
import io.bbs.seva.vbbs004mobile.presentation.root.AppRoot
import io.bbs.seva.vbbs004mobile.session.SessionManager
import io.bbs.seva.vbbs004mobile.ui.theme.Vbbs004MobileTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var sessionManager: SessionManager

    //@Inject
    //lateinit var profileDataStore: ProfileDataStore

    private var isReady = false

    //private val viewModel: HomeViewModel by viewModels()
    var initialAuthState: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        splashScreen.setKeepOnScreenCondition {
            !isReady
        }
        lifecycleScope.launch {
//            // .first() suspends until the Flow emits its first item
//            // We wait until it emits something that is NOT null
            initialAuthState = authRepository.isAuthenticated.first { it != null }
            authRepository.userProfile.first()
//            // Once we get a non-null value, update the flag
            isReady = true
            setContent {
                Vbbs004MobileTheme {
                    AppRoot(
                        authRepository = authRepository,
                        sessionManager = sessionManager,
                        //                homeViewModel = viewModel,
                        initialAuthState = initialAuthState,
                    )
                }
            }
        }

        // OPTIONAL: Keep splash screen on screen until auth state is loaded
        // Because you have a splash screen state in AppRoot, you can let the
        // system splash screen handle the loading instead of a blank Compose screen.
//        splashScreen.setKeepOnScreenCondition {
//            // Return true to keep the system splash visible, false to dismiss it immediately.
//            // If you want to wait for authRepository to load:
//            authRepository.isAuthenticated.value == null
//            // false
//        }


    }
}



