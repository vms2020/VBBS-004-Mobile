package io.bbs.seva.vbbs004mobile.presentation.screens.currency_rates

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bbs.seva.vbbs004mobile.data.remote.mapper.toAppError
import io.bbs.seva.vbbs004mobile.domain.model.AppError
import io.bbs.seva.vbbs004mobile.domain.model.CurrencyRatePoint
import io.bbs.seva.vbbs004mobile.domain.usecase.CurrencyRatesUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class CurrencyDynamicsState(
    val charCode: String = "",
    val name: String = "",
    val points: List<CurrencyRatePoint> = emptyList(),
    val from: LocalDate = LocalDate.now().minusDays(30),
    val to: LocalDate = LocalDate.now(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: AppError? = null,
)

@HiltViewModel
class CurrencyDynamicsViewModel @Inject constructor(
    private val currencyRates: CurrencyRatesUseCase,
) : ViewModel() {

    private val _state = MutableStateFlow(CurrencyDynamicsState())
    val state: StateFlow<CurrencyDynamicsState> = _state.asStateFlow()

    private var currencyId: String? = null

    private val _openIntervalPicker = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val openIntervalPicker: SharedFlow<Unit> = _openIntervalPicker

    fun requestIntervalPicker() {
        _openIntervalPicker.tryEmit(Unit)
    }

    /** Инициализация из экрана: параметры ключа навигации приходят явно. */
    fun start(currencyId: String, charCode: String, name: String) {
        this.currencyId = currencyId
        _state.update { it.copy(charCode = charCode, name = name) }
        load(showSpinner = true)
    }

    /** Pull-to-refresh: контент остаётся, тихий индикатор. */
    fun refresh() = load(showSpinner = false)

    /** С экрана ошибки: полноэкранная загрузка снова. */
    fun retry() = load(showSpinner = true)

    fun onIntervalSelected(from: LocalDate, to: LocalDate) {
        _state.update { it.copy(from = from, to = to) }
        load(showSpinner = true)
    }

    private fun load(showSpinner: Boolean) {
        val id = currencyId ?: return
        val from = _state.value.from
        val to = _state.value.to

        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = showSpinner, isRefreshing = !showSpinner, error = null)
            }

            currencyRates.dynamics(
                currencyId = id,
                from = from , //LocalDate.now().minusDays(30),
                to = to, //LocalDate.now(),
            )
                .onSuccess { points ->
                    _state.update {
                        it.copy(points = points, isLoading = false, isRefreshing = false)
                    }
                }
                .onFailure { e ->
                    _state.update {
                        it.copy(isLoading = false, isRefreshing = false, error = e.toAppError())
                    }
                }
        }
    }
}
