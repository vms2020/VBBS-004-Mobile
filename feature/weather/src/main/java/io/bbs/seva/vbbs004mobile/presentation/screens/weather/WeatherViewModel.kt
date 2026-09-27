package io.bbs.seva.vbbs004mobile.presentation.screens.weather

// presentation/screens/weather/WeatherViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bbs.seva.vbbs004mobile.data.remote.mapper.toAppError
import io.bbs.seva.vbbs004mobile.domain.repository.GeoLocationRepository
import io.bbs.seva.vbbs004mobile.domain.usecase.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val getWeatherUseCase: GetWeatherUseCase,
    private val geoLocationRepository: GeoLocationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(WeatherState())
    val state: StateFlow<WeatherState> = _state

    //init { fetchWeather() }
    init { load(showSpinner = true) }

    fun refresh() = load(showSpinner = false)
    fun retry()  = load(showSpinner = true)

    private fun load(showSpinner: Boolean) {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = showSpinner,
                    isRefreshing = !showSpinner,
                    error = null,
                )
            }

            // saved location drives the query; default if none saved
            val location = geoLocationRepository.savedGeoLocation.first()
            val lat = location?.lat
            val lon = location?.lon

            runCatching {
                if (lat != null && lon != null) {
                    getWeatherUseCase(lat, lon)
                } else {
                    getWeatherUseCase()
                }
            }.onSuccess { data ->
                // data : WeatherDashboard — тип даёт use case (см. ниже)
                _state.update {
                    it.copy(weather = data, isLoading = false, isRefreshing = false)
                }
            }.onFailure { e ->
                _state.update {
                    it.copy(isLoading = false, isRefreshing = false, error = e.toAppError())
                }
            }
        }
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
                //_state.value = WeatherState(error = e.localizedMessage ?: "Failed to load weather")
                _state.value = WeatherState(error = e.toAppError())
            }
        }
    }
}
