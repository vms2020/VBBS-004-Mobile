package io.bbs.seva.vbbs004mobile.presentation.screens.weather

// presentation/screens/weather/WeatherViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bbs.seva.vbbs004mobile.domain.repository.GeoLocationRepository
import io.bbs.seva.vbbs004mobile.domain.usecase.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val geoLocationRepository: GeoLocationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state

    init {
        fetchWeather()
    }

    fun fetchWeather() {
        viewModelScope.launch {
            _state.value = WeatherState(isLoading = true)
            val location = geoLocationRepository.savedGeoLocation.first()

            try {
                val data = if (location?.lat != null && location.lon != null) {
                    getWeatherUseCase(location.lat, location.lon)
                } else {
                    getWeatherUseCase()
                }
                _state.value = WeatherState(weather = data)
            } catch (e: Exception) {
                // If token refresh failed entirely, Ktor triggers an exception here
                _state.value = WeatherState(error = e.localizedMessage ?: "Failed to load weather")
            }
        }
    }
}
