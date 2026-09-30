package io.bbs.seva.vbbs004mobile.data.repository

import io.bbs.seva.vbbs004mobile.domain.repository.CurrencyRateRepository


//import io.bbs.seva.vbbs004mobile.BuildConfig
import io.bbs.seva.vbbs004mobile.core.network.BuildConfig
import io.bbs.seva.vbbs004mobile.data.remote.dto.cbr.daily.CbrDailyDto
import io.bbs.seva.vbbs004mobile.data.remote.dto.cbr.dynamic.CbrDynamicDto
import io.bbs.seva.vbbs004mobile.data.remote.mapper.toApiDateString
import io.bbs.seva.vbbs004mobile.data.remote.mapper.toDomainPoints
import io.bbs.seva.vbbs004mobile.data.remote.mapper.toDomainRates
import io.bbs.seva.vbbs004mobile.domain.model.CurrencyRatePoint
import io.bbs.seva.vbbs004mobile.domain.model.CurrencyDailyRates
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import java.time.LocalDate
import javax.inject.Inject

class RateRepositoryImpl @Inject constructor(
    private val httpClient: HttpClient,
) : CurrencyRateRepository {

    override suspend fun getDailyRates(date: LocalDate?): Result<CurrencyDailyRates> = runCatching {
        //val dto: CbrDailyDto = httpClient.get("${BuildConfig.BASE_URL}cbr/daily") {
        val dto: CbrDailyDto = httpClient.get("cbr/daily") {
            date?.let { parameter("date_req", it.toApiDateString()) }   // dd/MM/yyyy, optional
        }.body()
        dto.toDomainRates()
    }

    override suspend fun getRateDynamics(
        currencyId: String,
        from: LocalDate,
        to: LocalDate,
    ): Result<List<CurrencyRatePoint>> = runCatching {
        //val dto: CbrDynamicDto = httpClient.get("${BuildConfig.BASE_URL}cbr/dynamic") {
        val dto: CbrDynamicDto = httpClient.get("cbr/dynamic") {
            parameter("VAL_NM_RQ", currencyId)          // required
            parameter("date_req1", from.toApiDateString())
            parameter("date_req2", to.toApiDateString())
        }.body()
        dto.toDomainPoints()
    }
}
