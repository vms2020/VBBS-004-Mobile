package io.bbs.seva.vbbs004mobile.presentation.root

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bbs.seva.vbbs004mobile.domain.repository.AuthRepository
import io.bbs.seva.vbbs004mobile.session.SessionManager
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

// app/.../presentation/root/AppViewModel.kt  (new file)
@HiltViewModel
class AppViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    sessionManager: SessionManager,
) : ViewModel() {
    // adapt the mapping to your SessionManager's actual API (SharedFlow/StateFlow/Channel)
//////////////////////    val logoutEvents = sessionManager.logoutEvents
//        .map { Unit }
//        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    private val logoutChannel = Channel<Unit>(Channel.BUFFERED)
    val logoutEvents: Flow<Unit> = logoutChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            sessionManager.logoutEvents.collect { logoutChannel.send(Unit) }
        }
    }

    val isAuthenticated: StateFlow<Boolean?> = authRepository.isAuthenticated
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null,           // ← null = "not yet observed beyond the gate"
        )

    fun logout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }
}
