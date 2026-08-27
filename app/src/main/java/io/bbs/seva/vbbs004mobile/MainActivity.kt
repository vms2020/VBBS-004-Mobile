package io.bbs.seva.vbbs004mobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import io.bbs.seva.vbbs004mobile.domain.repository.AuthRepository
import io.bbs.seva.vbbs004mobile.presentation.root.AppRoot
import io.bbs.seva.vbbs004mobile.session.SessionManager
import io.bbs.seva.vbbs004mobile.ui.theme.Vbbs004MobileTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authRepository: AuthRepository

    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Vbbs004MobileTheme {
                AppRoot(
                    authRepository = authRepository,
                    sessionManager = sessionManager
                )
            }
        }
    }
}



