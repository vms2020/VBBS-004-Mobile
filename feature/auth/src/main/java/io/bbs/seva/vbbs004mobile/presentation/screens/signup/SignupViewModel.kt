package io.bbs.seva.vbbs004mobile.presentation.screens.signup
// presentation/screens/signup/SignupViewModel.kt

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bbs.seva.vbbs004mobile.domain.usecase.SignupUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase
) : ViewModel() {

    // Form state
    var email = MutableStateFlow("")
    var password = MutableStateFlow("")
    var fullName = MutableStateFlow("")
    var age = MutableStateFlow("")
    var avatarUrl = MutableStateFlow("")

    // UI State
    private val _uiState = MutableStateFlow<SignupUiState>(SignupUiState.Idle)
    val uiState: StateFlow<SignupUiState> = _uiState.asStateFlow()

    fun signup() {
        if (email.value.isBlank() || password.value.isBlank()) {
            _uiState.update { SignupUiState.Error("Email and password are required") }
            return
        }

        val ageInt = age.value.toIntOrNull()

        _uiState.update { SignupUiState.Loading }
        viewModelScope.launch {
            val params = SignupUseCase.Params(
                email = email.value.trim(),
                password = password.value,
                fullName = fullName.value.ifBlank { null },
                age = ageInt,
                avatarUrl = avatarUrl.value.ifBlank { null }
            )
            val result = signupUseCase(params)


            if (result.isSuccess) {
                // If your backend automatically logs the user in, AppRoot will react
                // to isAuthenticated changing and navigate automatically.
                _uiState.update { SignupUiState.Success("Account created successfully!") }
            } else {
                val error = result.exceptionOrNull()?.message ?: "Unknown error"
                _uiState.update { SignupUiState.Error(error) }
            }
        }
    }

    fun resetState() {
        _uiState.update { SignupUiState.Idle }
    }
}
