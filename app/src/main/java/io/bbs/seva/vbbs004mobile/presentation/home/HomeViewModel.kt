package io.bbs.seva.vbbs004mobile.presentation.home

// presentation/home/HomeViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bbs.seva.vbbs004mobile.domain.model.User
import io.bbs.seva.vbbs004mobile.domain.usecase.HomeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
//import kotlinx.flow.MutableStateFlow
//import kotlinx.flow.StateFlow
//import kotlinx.flow.asStateFlow
//import kotlinx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase
) : ViewModel() {

    private val _isRefreshing = MutableStateFlow(false)
    // Combine local DataStore stream with network refreshing indicators
    val uiState: StateFlow<HomeUiState> = combine(
        homeUseCase.userProfile,
        _isRefreshing
    ) { user, refreshing ->
        HomeUiState(
            user = user,
            isRefreshing = refreshing,
            error = null
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = HomeUiState()
    )

    // Triggered on app launch or when pulling to refresh
    fun refreshProfile() {
        viewModelScope.launch {
            _isRefreshing.value = true
            homeUseCase.refreshProfile()
                .onFailure { /* Optionally emit error to your state here */ }
            _isRefreshing.value = false
        }
    }
}

//sealed interface HomeUiState {
//    object Idle : HomeUiState
//    object Loading : HomeUiState
//    object Success : HomeUiState
//    data class Error(val message: String) : HomeUiState
//}

data class HomeUiState(
    val user: User? = null,
    val isRefreshing: Boolean = false,
    val error: String? = null
)
