package io.bbs.seva.vbbs004mobile.presentation.screens.login

// presentation/login/LoginViewModel.kt
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bbs.seva.vbbs004mobile.domain.usecase.LoginUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
//import kotlinx.flow.MutableStateFlow
//import kotlinx.flow.StateFlow
//import kotlinx.flow.asStateFlow
//import kotlinx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import javax.inject.Inject
import kotlinx.coroutines.launch

private const val TAG = "LoginViewModel"

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Idle)
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(email: String, password: String, onLoginSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.value = LoginUiState.Loading
            loginUseCase(email, password)
                .onSuccess {
                    _uiState.value = LoginUiState.Success
                    onLoginSuccess()
                }
                .onFailure { error ->
                    Log.d(TAG, "login: $error")
                    _uiState.value = LoginUiState.Error(error.localizedMessage ?: "Unknown Error")
                }
        }
    }
}

sealed interface LoginUiState {
    object Idle : LoginUiState
    object Loading : LoginUiState
    object Success : LoginUiState
    data class Error(val message: String) : LoginUiState
}
