package io.bbs.seva.vbbs004mobile.domain.usecase

import io.bbs.seva.vbbs004mobile.domain.model.CurrencyDailyRates
import io.bbs.seva.vbbs004mobile.domain.model.CurrencyRatePoint
import io.bbs.seva.vbbs004mobile.domain.repository.CurrencyRateRepository
import java.time.LocalDate
import javax.inject.Inject

// CurrencyRatesUseCase.kt — :core:domain/usecase/
class CurrencyRatesUseCase @Inject constructor(
    private val repository: CurrencyRateRepository,
) {
    /** Daily table for `date`, or the latest published set when null. */
    suspend fun daily(date: LocalDate? = null): Result<CurrencyDailyRates> =
        repository.getDailyRates(date)

    /** History of one currency (CurrencyRate.id) over [from, to]. */
    suspend fun dynamics(
        currencyId: String,
        from: LocalDate,
        to: LocalDate,
    ): Result<List<CurrencyRatePoint>> =
        repository.getRateDynamics(currencyId, from, to)
}
