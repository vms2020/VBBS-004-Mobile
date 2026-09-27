package io.bbs.seva.vbbs004mobile.domain.model

import java.math.BigDecimal
import java.time.LocalDate

data class CurrencyRatePoint(
    val date: LocalDate,
    val perUnit: BigDecimal,
)
