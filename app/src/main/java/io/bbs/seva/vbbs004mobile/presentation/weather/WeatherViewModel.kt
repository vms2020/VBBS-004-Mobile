package io.bbs.seva.vbbs004mobile.presentation.weather

// presentation/weather/WeatherViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bbs.seva.vbbs004mobile.domain.usecase.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state

    init { fetchWeather() }

    fun fetchWeather() {
        viewModelScope.launch {
            _state.value = WeatherState(isLoading = true)
            try {
                val data = getWeatherUseCase()
                _state.value = WeatherState(weather = data)
            } catch (e: Exception) {
                // If token refresh failed entirely, Ktor triggers an exception here
                _state.value = WeatherState(error = e.localizedMessage ?: "Failed to load weather")
            }
        }
    }
}
