package io.bbs.seva.vbbs004mobile.session
// session/SessionManager.kt

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager @Inject constructor() {
    private val _logoutEvents = MutableSharedFlow<Unit>()
    val logoutEvents = _logoutEvents.asSharedFlow()

    suspend fun emitLogout() {
        _logoutEvents.emit(Unit)
    }
}