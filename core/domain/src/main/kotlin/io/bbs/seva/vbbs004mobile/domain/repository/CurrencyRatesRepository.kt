package io.bbs.seva.vbbs004mobile.domain.repository

// core/domain/src/main/kotlin/io/bbs/seva/vbbs004mobile/domain/repository/RateRepository.kt

import io.bbs.seva.vbbs004mobile.domain.model.CurrencyRatePoint
import io.bbs.seva.vbbs004mobile.domain.model.CurrencyDailyRates
import java.time.LocalDate

interface CurrencyRateRepository {
    /** Official CBR rates for one day; null = latest published set. */
    suspend fun getDailyRates(date: LocalDate? = null): Result<CurrencyDailyRates>

    /** Dynamics of one currency's per-unit rate over a date range. */
    suspend fun getRateDynamics(
        currencyId: String,          // CBR numeric id, e.g. "R01235" (USD)
        from: LocalDate,
        to: LocalDate,
    ): Result<List<CurrencyRatePoint>>
}
