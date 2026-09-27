package io.bbs.seva.vbbs004mobile.domain.model

import java.math.BigDecimal

data class CurrencyRate(
    val id: String,
    val charCode: String,
    val name: String,
    val nominal: Int,
    val value: BigDecimal,      // per Nominal units
    val perUnit: BigDecimal,    // per 1 unit — the display value
)
