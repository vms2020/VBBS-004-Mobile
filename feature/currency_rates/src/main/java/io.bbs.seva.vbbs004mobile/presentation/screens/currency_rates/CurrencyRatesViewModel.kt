package io.bbs.seva.vbbs004mobile.presentation.screens.currency_rates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bbs.seva.vbbs004mobile.domain.model.CurrencyDailyRates
import io.bbs.seva.vbbs004mobile.domain.model.AppError
import io.bbs.seva.vbbs004mobile.domain.usecase.CurrencyRatesUseCase
import io.bbs.seva.vbbs004mobile.data.remote.mapper.toAppError
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class CurrencyRatesUiState(
    val dailyRates: CurrencyDailyRates? = null,
    val isFilteredByDate: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: AppError? = null,
)

@HiltViewModel
class CurrencyRatesViewModel @Inject constructor(
    private val currencyRates: CurrencyRatesUseCase,
) : ViewModel() {
    // CurrencyRatesViewModel:
    private val _openDatePicker = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val openDatePicker: SharedFlow<Unit> = _openDatePicker

    fun requestDatePicker() {
        _openDatePicker.tryEmit(Unit)
    }

    private val _uiState = MutableStateFlow(CurrencyRatesUiState(isLoading = true))
    val uiState: StateFlow<CurrencyRatesUiState> = _uiState.asStateFlow()


    fun refresh() = load(showSpinner = false)
    fun retry() = load(showSpinner = true)

    private fun load(showSpinner: Boolean) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isLoading = showSpinner, isRefreshing = !showSpinner, error = null)
            }

            currencyRates.daily(_selectedDate.value)
                .onSuccess { daily ->
                    _uiState.update {
                        it.copy(
                            dailyRates = daily,
                            isFilteredByDate = _selectedDate.value != null,
                            isLoading = false,
                            isRefreshing = false,
                        )
                    }
                }
                .onFailure { e ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRefreshing = false,
                            error = e.toAppError(),
                        )
                    }
                }
        }
    }

    private val _selectedDate = MutableStateFlow<LocalDate?>(null)

    fun onDateSelected(date: LocalDate?) {
        _selectedDate.value = date
        load(showSpinner = true)
    }

    fun onDateCleared() {
        _selectedDate.value = null
        load(showSpinner = true)
    }

    init {
        load(showSpinner = true)
    }

}
