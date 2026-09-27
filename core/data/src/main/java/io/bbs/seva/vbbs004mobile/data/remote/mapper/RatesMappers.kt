package io.bbs.seva.vbbs004mobile.data.remote.mapper

import io.bbs.seva.vbbs004mobile.data.remote.dto.cbr.daily.CbrDailyDto
import io.bbs.seva.vbbs004mobile.data.remote.dto.cbr.dynamic.CbrDynamicDto
import io.bbs.seva.vbbs004mobile.data.remote.dto.cbr.dynamic.RecordDto
import io.bbs.seva.vbbs004mobile.data.remote.dto.cbr.daily.ValuteDto
import io.bbs.seva.vbbs004mobile.domain.model.CurrencyDailyRates
import io.bbs.seva.vbbs004mobile.domain.model.CurrencyRate
import io.bbs.seva.vbbs004mobile.domain.model.CurrencyRatePoint
import java.math.BigDecimal
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val RU_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
private val API_DATE: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

/** "71,5532" → 71.5532 (CBR uses comma decimals); null-safe at the boundary. */
private fun String?.toDecimal(): BigDecimal? =
    this?.replace(',', '.')?.let { runCatching { BigDecimal(it) }.getOrNull() }

private fun String?.toLocalDate(): LocalDate? =
    this?.let { runCatching { LocalDate.parse(it, RU_DATE) }.getOrNull() }

fun CbrDailyDto.toDomainRates(): CurrencyDailyRates =
    CurrencyDailyRates(
        date = date.toLocalDate(),
        rates = valute.orEmpty().mapNotNull { it?.toDomain() },
    )

private fun ValuteDto.toDomain(): CurrencyRate? {
    val perUnit = vunitRate.toDecimal() ?: return null      // no number → row unusable
    val value = value.toDecimal() ?: return null
    val charCode = charCode?.takeIf { it.isNotBlank() } ?: return null
    return CurrencyRate(
        id = iD ?: return null,
        charCode = charCode,
        name = name.orEmpty(),
        nominal = nominal?.toInt() ?: 1,
        value = value,
        perUnit = perUnit,
    )
}

fun CbrDynamicDto.toDomainPoints(): List<CurrencyRatePoint> =
    record.orEmpty()
        .mapNotNull { dto: RecordDto? ->
            // Added explicit null-safe navigation (?) to fix compilation errors
            val perUnit = dto?.vunitRate.toDecimal() ?: return@mapNotNull null
            val d = dto?.date.toLocalDate() ?: return@mapNotNull null
            CurrencyRatePoint(date = d, perUnit = perUnit)
        }

// API request format (dd/MM/yyyy) — used by the impl when building date_req params:
fun LocalDate.toApiDateString(): String = format(API_DATE)
