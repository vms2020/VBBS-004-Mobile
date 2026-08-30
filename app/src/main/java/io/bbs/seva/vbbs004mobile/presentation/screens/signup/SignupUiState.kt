package io.bbs.seva.vbbs004mobile.presentation.screens.signup
// presentation/screens/signup/SignupUiState.kt

sealed interface SignupUiState {
    data object Idle : SignupUiState
    data object Loading : SignupUiState
    data class Error(val message: String) : SignupUiState
    data class Success(val message: String) : SignupUiState
}
