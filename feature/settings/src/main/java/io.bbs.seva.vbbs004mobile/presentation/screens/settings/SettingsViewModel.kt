package io.bbs.seva.vbbs004mobile.presentation.screens.settings


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bbs.seva.vbbs004mobile.domain.usecase.GetSettingsUseCase
import io.bbs.seva.vbbs004mobile.domain.usecase.SaveSettingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val saveSettingsUseCase: SaveSettingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadCurrentSettings()
    }

    private fun loadCurrentSettings() {
        viewModelScope.launch {
            // Read current parameters via the Use Case invocation
            val currentSettings = getSettingsUseCase().first()

            _uiState.update { currentState ->
                currentState.copy(
                    baseUrl = currentSettings.baseUrl,
                    isProxyEnabled = currentSettings.proxy.isEnabled,
                    proxyProtocol = currentSettings.proxy.protocol,
                    proxyHost = currentSettings.proxy.host,
                    proxyPort = currentSettings.proxy.port.toString()
                )
            }
        }
    }

    fun onBaseUrlChanged(newUrl: String) {
        _uiState.update { it.copy(baseUrl = newUrl, saveSuccess = false) }
    }

    fun onProxyToggleChanged(enabled: Boolean) {
        _uiState.update { it.copy(isProxyEnabled = enabled, saveSuccess = false) }
    }

    fun onProxyProtocolChanged(protocol: String) {
        _uiState.update { it.copy(proxyProtocol = protocol, saveSuccess = false) }
    }

    fun onProxyHostChanged(host: String) {
        _uiState.update { it.copy(proxyHost = host, saveSuccess = false) }
    }

    fun onProxyPortChanged(port: String) {
        if (port.isEmpty() || port.all { it.isDigit() }) {
            _uiState.update { it.copy(proxyPort = port, saveSuccess = false) }
        }
    }

    fun saveSettings() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val state = _uiState.value

            // Execute the save operation via the Use Case and handle success/failures gracefully
            val result = saveSettingsUseCase(
                baseUrl = state.baseUrl,
                proxyEnabled = state.isProxyEnabled,
                proxyProtocol = state.proxyProtocol,
                proxyHost = state.proxyHost,
                proxyPortStr = state.proxyPort
            )

            result.onSuccess {
                _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
            }.onFailure { exception ->
                _uiState.update { it.copy(isSaving = false, saveSuccess = false) }
                // Optional: You could extend SettingsUiState to show an error message via exception.message
            }
        }
    }
}


data class SettingsUiState(
    val baseUrl: String = "",
    val isProxyEnabled: Boolean = false,
    val proxyProtocol: String = "SOCKS", // HTTP or SOCKS
    val proxyHost: String = "",
    val proxyPort: String = "", // Kept as String for raw text field input
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false
)



