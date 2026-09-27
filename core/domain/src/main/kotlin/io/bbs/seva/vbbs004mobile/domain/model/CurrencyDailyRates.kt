package io.bbs.seva.vbbs004mobile.domain.model

// core/domain/src/main/kotlin/io/bbs/seva/vbbs004mobile/domain/model/DailyRates.kt

import java.time.LocalDate

/** One day's official rates, with the effective date the bank stamped them with. */
data class CurrencyDailyRates(
    val date: LocalDate?,     // null only if the converter omitted it — honest at the edge
    val rates: List<CurrencyRate>,
)